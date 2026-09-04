package org.example.knockin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BlockErrorCode implements ErrorCode {
    NOT_FOUND(20000, HttpStatus.NOT_FOUND, "차단 기록을 찾을수 없습니다."),
    DUPLICATE(20001, HttpStatus.BAD_REQUEST, "이미 차단한 사용자입니다."),
    ;

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
