package org.example.knockin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AgreementErrorCode implements ErrorCode {
    AGREEMENT_NOT_FOUNT(14000, HttpStatus.INTERNAL_SERVER_ERROR, "약관 정보를 조회하지 못하였습니다."),
    AGREEMENT_TYPE_NOT_FOUNT(14001, HttpStatus.INTERNAL_SERVER_ERROR, "약관 유형 정보를 조회하지 못하였습니다."),
    AGREEMENT_LOG_NOT_FOUNT(14002, HttpStatus.INTERNAL_SERVER_ERROR, "약관 기록 정보를 조회하지 못하였습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
