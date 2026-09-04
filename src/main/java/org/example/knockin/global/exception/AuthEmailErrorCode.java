package org.example.knockin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthEmailErrorCode implements ErrorCode {
    AUTH_EMAIL_NOT_FOUND(13000, HttpStatus.NOT_FOUND, "인증 이메일 정보를 찾지 못하였습니다."),
    AUTH_EMAIL_DUPLICATE_DOMAIN(13001, HttpStatus.CONFLICT, "이미 등록된 도메인입니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
