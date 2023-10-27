package com.ssafy.eoullim.exception;

import com.ssafy.eoullim.dto.response.ErrorResponse;
import com.ssafy.eoullim.dto.response.Response;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ssafy.eoullim.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EoullimApplicationException.class)
  public ResponseEntity<ErrorResponse> errorHandler(EoullimApplicationException e) {
    final var errorResponse =
        ErrorResponse.builder()
            .status(e.getErrorCode().getStatus())
            .code(e.getErrorCode().getCode())
            .message(e.getErrorCode().name() + " : " + e.getMessage()) // 클라이언트에게는 에러 코드만.
            .build();
    // 여기서 ERROR CODE 안에 있는 status랑 message, 또한 추가로 exception으로 온 message
    log.error("Error occurs {}", e.toString()); // 서버에 exception 메시지 출력
    log.error("Error occurs in method: " + e.getStackTrace()[0]);
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse requestValidationHandler(MethodArgumentNotValidException e) {
    StringBuilder errorMsg = new StringBuilder();
    errorMsg.append("Failed Validation of Request Body.").append("\n");

    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    for (FieldError fieldError : fieldErrors) {   // 별도로 메시지가 있는 경우만 추가
      if (fieldError.getDefaultMessage() == null) {
        errorMsg.append(fieldError.getCode());
      }
    }

    final var errorResponse =
            ErrorResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                    .message(errorMsg.toString())
                    .build();
    log.error("Error occurs {}", e.toString());
    log.error("Error occurs in method: " + e.getStackTrace()[0]);
    return errorResponse;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> badRequestHandler(IllegalArgumentException e) {
    final var errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST)
            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .message(e.getMessage())
            .build();
    log.error("Error occurs {}", e.toString());
    log.error("Error occurs in method: " + e.getStackTrace()[0]);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAll(Exception e) {
    final var errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
            .message(e.getMessage())
            .build();
    log.error("Error occurs {}", e.toString());
    log.error("Error occurs in method: " + e.getStackTrace()[0]);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<ErrorResponse> serverErrorHandler(IOException e) {
    final var errorResponse =
        ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
            .message(e.getMessage())
            .build();
    log.error("Error occurs {}", e.toString());
    log.error("Error occurs in method: " + e.getStackTrace()[0]);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler({
    OpenViduHttpException.class,
    OpenViduJavaClientException.class,
    InterruptedException.class
  })
  public ResponseEntity<?> openviduErrorHandler(OpenViduHttpException e) {
    log.error("Error occurs {}", e.toString());
    log.error("Error occurs in method: " + e.getStackTrace()[0]);
    return ResponseEntity.status(OPENVIDU_HTTP_ERROR.getStatus())
        .body(Response.error(OPENVIDU_HTTP_ERROR.name()));
  }
}
