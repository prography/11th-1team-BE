package org.example.knockin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MetaErrorCode implements ErrorCode {
    REGION_NOT_FOUND(4000, HttpStatus.NOT_FOUND, "지역 조회에 실패하였습니다."),
    ROOM_TYPE_NOT_FOUND(4001, HttpStatus.NOT_FOUND, "방 형태 조회에 실패하였습니다."),
    TERM_NOT_FOUND(4002, HttpStatus.NOT_FOUND, "약관동의 조회에 실패하였습니다."),
    EXTRA_OPTION_NOT_FOUND(4001, HttpStatus.NOT_FOUND, "방 추가 옵션 조회에 실패하였습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
