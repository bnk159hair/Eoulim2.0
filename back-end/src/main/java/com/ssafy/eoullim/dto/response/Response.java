package com.ssafy.eoullim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Response<Object> {

    private final HttpStatus status;
    private final String message;
    private final Object content;

    public static <Object> Response<Object> success() {
        return new Response<Object>(HttpStatus.OK,"success", null);
    }

    public static <Object> Response<Object> success(Object content) {
        return new Response<Object>(HttpStatus.OK,"success", null);
    }

    public static <Object> Response<Object> success(String message, Object content) {
        return new Response<Object>(HttpStatus.OK,"success", null);
    }

    public static <Object> Response<Object> success(HttpStatus status, String message) {
        return new Response<Object>(status, message, null);
    }

    public static <Object> Response<Object> success(HttpStatus status, String message, Object content) {
        return new Response<Object>(status, message, content);
    }

    public static Response<Void> error(String resultCode) {
        return new Response<Void>(null, resultCode, null);
    }


    public String toStream() {
        if (content == null) {
            return "{" +
                    "\"status\":" + "\"" + status + "\"," +
                    "\"message\":" + "\"" + message + "\"," +
                    "}";
        }
        return "{" +
                "\"status\":" + "\"" + status + "\"," +
                "\"message\":" + "\"" + message + "\"," +
                content +
                "}";
    }
}
