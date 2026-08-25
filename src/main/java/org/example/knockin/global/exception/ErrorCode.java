package org.example.knockin.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    Integer getNo();
    String name();
    HttpStatus getHttpStatus();
    String getMessage();

    default String format(Object... args) {
        return getMessage().formatted(args);
    }
}
