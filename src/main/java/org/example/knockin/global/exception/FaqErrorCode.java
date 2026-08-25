package org.example.knockin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FaqErrorCode implements ErrorCode {
    ALREADY_EXIST_SORT(11000, HttpStatus.BAD_REQUEST, "이미 등록된 정렬 순서입니다."),
    FAQ_NOT_FOUND(11001, HttpStatus.BAD_REQUEST, "자주 찾는 질문 게시물을 찾을수 없습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
