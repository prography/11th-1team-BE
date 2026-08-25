package org.example.knockin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    BAD_REQUEST(3000, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(3001, HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    METHOD_NOT_ALLOWED(3002, HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    UNSUPPORTED_MEDIA_TYPE(3003, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 Content-Type입니다."),
    ACCESS_DENIED(3004, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(3005, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
