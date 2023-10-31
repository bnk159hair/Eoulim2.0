package com.ssafy.eoullim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.TimeZone;

@Data
@NoArgsConstructor
public class SuccessResponse<T> extends CommonResponse {
  private T data;

  // status, code, data로 생성자
  public SuccessResponse(String status, Integer code, T data) {
    super(ZonedDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId()), status, String.valueOf(code));
    this.data = data;
  }

  // OK Response는 자주 쓰니까 별도 생성자
  public SuccessResponse(T data) {
    super(
        ZonedDateTime.now(TimeZone.getTimeZone("Asia/Seoul").toZoneId()),
        HttpStatus.OK.name(),
        String.valueOf(HttpStatus.OK.value()));
    this.data = data;
  }
}
