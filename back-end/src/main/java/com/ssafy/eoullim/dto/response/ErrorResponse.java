package com.ssafy.eoullim.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class ErrorResponse extends CommonResponse {    // 반환할 Error Response 객체
    private String message;

    @Builder
    public ErrorResponse(ZonedDateTime timeStamp, String status, String code,  String message){
        super(timeStamp, status, code);
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"timestamp\":" + "\"" + this.getTimeStamp() + "\"," +
                "\"status\":" + "\"" + this.getStatus() + "\"," +
                "\"code\":" + "\"" + this.getCode() + "\"," +
                "\"message\":" + "\"" + this.getMessage() + "\"," +
                "}";
    }
}