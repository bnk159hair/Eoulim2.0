package com.ssafy.eoullim.exception;

import com.ssafy.eoullim.dto.response.ErrorResponse;
import com.ssafy.eoullim.dto.response.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    final var errorResponse =
        ErrorResponse.builder()
            .status(ErrorCode.UNAUTHORIZED_TOKEN.getStatus().toString())
            .code(ErrorCode.UNAUTHORIZED_TOKEN.getCode())
            .message(
                ErrorCode.UNAUTHORIZED_TOKEN.name()
                    + " : "
                    + ErrorCode.UNAUTHORIZED_TOKEN.getMessage())
            .timeStamp(ZonedDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId()))
            .build();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");
    response.setStatus(ErrorCode.UNAUTHORIZED_TOKEN.getStatus().value());
    response.getWriter().write(errorResponse.toString());
    //
    // response.getWriter().write(Response.error(ErrorCode.UNAUTHORIZED_TOKEN.name()).toStream());
  }
}
