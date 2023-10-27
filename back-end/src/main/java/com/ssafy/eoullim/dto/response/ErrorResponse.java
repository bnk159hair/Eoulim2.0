package com.ssafy.eoullim.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ErrorResponse {    // 반환할 Error Response 객체
    private HttpStatus status;
    private String code;
    private String message;

    @Builder
    public ErrorResponse(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"status\":" + "\"" + status + "\"," +
                "\"code\":" + "\"" + code + "\"," +
                "\"message\":" + "\"" + message + "\"," +
                "}";
    }
}