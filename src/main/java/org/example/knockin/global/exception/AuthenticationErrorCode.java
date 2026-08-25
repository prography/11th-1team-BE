package org.example.knockin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthenticationErrorCode implements ErrorCode {
    AUTHENTICATION_NOT_FOUNT(17000, HttpStatus.INTERNAL_SERVER_ERROR, "인증 정보를 조회하지 못하였습니다."),
    AUTHENTICATION_APPROVE_NOT_FOUNT(17001, HttpStatus.INTERNAL_SERVER_ERROR, "인증 승인/거절 정보를 조회하지 못하였습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
