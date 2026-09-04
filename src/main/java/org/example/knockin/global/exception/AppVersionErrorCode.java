package org.example.knockin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AppVersionErrorCode implements ErrorCode {
    APP_VERSION_NOT_FOUND(12000, HttpStatus.BAD_REQUEST, "앱 버전을 조회에 실패하였습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
