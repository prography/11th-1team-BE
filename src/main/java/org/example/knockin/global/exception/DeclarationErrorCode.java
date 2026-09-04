package org.example.knockin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DeclarationErrorCode implements ErrorCode{
    DECLARATION_NOT_FOUND(18000, HttpStatus.NOT_FOUND, "신고 내역 조회에 실패하였습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
