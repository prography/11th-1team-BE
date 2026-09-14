package org.example.knockin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EtcErrorCode implements ErrorCode {
    SCORE_CALC_ERROR(22000, HttpStatus.BAD_REQUEST, "점수 계산 도중 알수없는 오류가 발생하였습니다."),
    ;

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
