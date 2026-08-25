package org.example.knockin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    TOKEN_EXPIRED(1, HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다."),
    TOKEN_INVALID(2, HttpStatus.UNAUTHORIZED, "인증 토큰이 유효하지 않습니다."),
    AUTHENTICATION_FAILED(3, HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    OAUTH_LOGIN_FAILED(4, HttpStatus.UNAUTHORIZED, "소셜 로그인에 실패했습니다."),
    MEMBER_NOT_FOUND(5, HttpStatus.UNAUTHORIZED, "회원을 찾을수 없습니다."),
    OAUTH_UNLINK_FAIL(6, HttpStatus.UNAUTHORIZED, "Oauth2 서버에 unlink API호출에 실패했습니다."),
    ILLEGAL_LOGIN_ACCESS(7, HttpStatus.UNAUTHORIZED, "비정상적인 동작으로 인해 로그인에 실패했습니다."),
    APPLE_JWT_DECODE_FAIL(8, HttpStatus.UNAUTHORIZED, "APPLE JWT 복호화에 실패했습니다."),
    APPLE_VALIDATE_JWT_ERROR(9, HttpStatus.UNAUTHORIZED, "올바르지 않은 JWT 토큰입니다."),
    APPLE_TOKEN_PARSE_ERROR(10, HttpStatus.UNAUTHORIZED, "Apple id_token 파싱 중 오류가 발생했습니다."),
    NOT_SUPPORT_SOCIAL_LOGIN(11, HttpStatus.UNAUTHORIZED, "Apple id_token 파싱 중 오류가 발생했습니다."),
    ACCESS_TOKEN_OMISSION(12, HttpStatus.UNAUTHORIZED, "Access Token이 누락되었습니다."),
    APPLE_CLIENT_SECRET_MAKE_FAIL(13, HttpStatus.UNAUTHORIZED, "Apple client_secret 생성 실패했습니다."),
    MEMBER_IS_DELETE(14, HttpStatus.UNAUTHORIZED, "이용이 정지된 계정입니다. "),
    ;

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
