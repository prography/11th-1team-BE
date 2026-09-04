package org.example.knockin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EmailErrorCode implements ErrorCode {
    EMAIL_SEND_ERROR(10000, HttpStatus.NOT_FOUND, "이메일 전송에 실패하였습니다."),
    EMAIL_WEBHOOK_ERROR(10001, HttpStatus.NOT_FOUND, "웹훅에 실패하였습니다."),
    EMAIL_TEMPLATE_LOAD_ERROR(10002, HttpStatus.NOT_FOUND, "인증메일 양식을 읽는데 실패하였습니다."),
    EMAIL_VALID_ERROR(10003, HttpStatus.NOT_FOUND, "인증 정보가 맞지 않아 실패하였습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
