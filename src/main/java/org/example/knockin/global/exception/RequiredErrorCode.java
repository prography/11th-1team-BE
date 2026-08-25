package org.example.knockin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RequiredErrorCode implements ErrorCode {
    CHATTING_NOT_FOUND(17000, HttpStatus.NOT_FOUND, "채팅 매칭 요청 조회에 실패하였습니다."),
    CHATTING_DUPLICATE(17001, HttpStatus.NOT_FOUND, "이미 대기중인 채팅 요청이 존재합니다."),
    CHATTING_ACCESS_DENIED(17002, HttpStatus.FORBIDDEN, "해당 채팅 매칭 요청에 대한 권한이 없습니다."),
    CHATTING_INVALID_STATUS(17003, HttpStatus.CONFLICT, "이미 처리되었거나 변경할 수 없는 채팅 매칭 요청 상태입니다."),
    ROOMMATE_NOT_FOUND(17004, HttpStatus.NOT_FOUND, "룸메이트 매칭 요청 조회에 실패하였습니다."),
    ROOMMATE_DUPLICATE(17005, HttpStatus.NOT_FOUND, "이미 대기중인 룸메이트 요청이 존재합니다."),
    ROOMMATE_ACCESS_DENIED(17006, HttpStatus.FORBIDDEN, "해당 룸메이트 매칭 요청에 대한 권한이 없습니다."),
    ROOMMATE_INVALID_STATUS(17007, HttpStatus.CONFLICT, "이미 처리되었거나 변경할 수 없는 룸메이트 매칭 요청 상태입니다."),
    ROOMMATE_ALREADY_EXISTS(17008, HttpStatus.CONFLICT, "이미 활성화된 룸메이트가 존재합니다."),
    ;

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
