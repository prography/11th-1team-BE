package org.example.knockin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MyRoommateErrorCode implements ErrorCode {
    NOT_FOUND(19000, HttpStatus.NOT_FOUND, "지역 조회에 실패하였습니다."),
    HOUSE_RULE_NOT_FOUND(19001, HttpStatus.NOT_FOUND, "하우스룰 조회에 실패하였습니다."),
    HOUSE_RULE_ACCESS_DENIED(19002, HttpStatus.FORBIDDEN, "접근할 수 없는 하우스룰 입니다."),
    HOUSE_RULE_DELETED(19003, HttpStatus.GONE, "삭제된 하우스룰 입니다."),
    CALENDER_NOT_FOUND(19004, HttpStatus.NOT_FOUND, "캘린더 조회에 실패하였습니다."),
    CALENDER_ACCESS_DENIED(19005, HttpStatus.FORBIDDEN, "접근할 수 없는 캘린더 입니다."),
    CALENDER_NOT_REPEAT(19006, HttpStatus.CONFLICT, "반복 일정이 아닙니다."),
    ;

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
