package com.ssafy.eoullim.exception;

import com.ssafy.eoullim.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  // Custom Exception
  @ExceptionHandler(EoullimApplicationException.class)
  public ResponseEntity<ErrorResponse> customExceptionHandler(EoullimApplicationException e) {
    final var errorResponse = getErrorResponse(e);
    printLog(e);
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorResponse);
  }

  // Validation Exception, 부적절한 request 요청일 때.
  @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse badRequestExceptionHandler(Exception e) {
    final var errorResponse = getErrorResponse(e);
    printLog(e);
    return errorResponse;
  }

  // ETC Exception
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorResponse etcExceptionHandler(Exception e) {
    final var errorResponse = getErrorResponse(e);
    printLog(e);
    e.printStackTrace();
    return errorResponse;
  }

  private void printLog(Exception e) {
    log.error("[EXCEPTION] 내용 : {}", e.toString());
    StackTraceElement[] stackTraceElements = e.getStackTrace();
    StackTraceElement element = stackTraceElements[0];
    log.error(
        String.format(
            "[EXCEPTION] 위치 : 클래스 - %s, 발생 메서드 - %s, in Line (%d)",
            element.getClassName(), element.getMethodName(), element.getLineNumber()));
  }

  private ErrorResponse getErrorResponse(Exception e) {
    String status = "", code = "", message = "";
    HttpStatus httpStatus = null;

    if (e instanceof EoullimApplicationException) {
      final var exception = (EoullimApplicationException) e;
      status = exception.getErrorCode().getStatus().toString();
      code = exception.getErrorCode().getCode();
      message = exception.getErrorCode().name() + " : " + e.getMessage();
    } else if (e instanceof MethodArgumentNotValidException) {
      final var exception = (MethodArgumentNotValidException) e;
      httpStatus = HttpStatus.BAD_REQUEST;

      StringBuilder eMsg = new StringBuilder();
      eMsg.append("FAILED VALIDATION of Request Body : ").append("\n");
      // 어떤 유효성 검사를 실패 했는 지 나열
      List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
      for (FieldError fieldError : fieldErrors) {
        eMsg.append(fieldError.getDefaultMessage()).append("\n");
      }
      message = eMsg.toString();
    } else if (e instanceof IllegalArgumentException) {
      httpStatus = HttpStatus.BAD_REQUEST;
    } else { // 나머지 전체 Exception
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    if (status.isBlank()) status = httpStatus.toString();
    if (code.isBlank()) code = String.valueOf(httpStatus.value());
    if (message.isBlank()) message = e.getMessage();

    return ErrorResponse.builder()
        .status(status)
        .code(code)
        .message(message) // 클라이언트에게는 에러 코드만.
        .timeStamp(ZonedDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId()))
        .build();
  }
}
