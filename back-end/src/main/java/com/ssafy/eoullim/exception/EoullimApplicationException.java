package com.ssafy.eoullim.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EoullimApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
    private String message;     // 이건 Exception이 발생했을 때, 개발자가 보기 위한 message

    public EoullimApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage() {
        if (message == null) {
            return errorCode.getMessage();
        } else {
            return String.format("%s: %s", errorCode.getMessage(), message);
        }
    }
}
