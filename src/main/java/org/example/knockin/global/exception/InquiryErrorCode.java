package org.example.knockin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InquiryErrorCode implements ErrorCode{
    INQUIRY_CATEGORY_NOT_FOUND(7000, HttpStatus.NOT_FOUND, "문의 카테고리 조회에 실패하였습니다."),
    INQUIRY_NOT_FOUND(7001, HttpStatus.NOT_FOUND, "문의 정보 조회에 실패하였습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
