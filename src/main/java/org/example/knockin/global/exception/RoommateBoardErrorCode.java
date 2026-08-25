package org.example.knockin.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoommateBoardErrorCode implements ErrorCode {
    ROOMMATE_BOARD_NOT_FOUND(2000, HttpStatus.NOT_FOUND, "룸메이트 게시글 조회에 실패했습니다."),
    ROOMMATE_BOARD_FORBIDDEN(2001, HttpStatus.FORBIDDEN, "룸메이트 게시글에 대한 권한이 없습니다"),
    ROOMMATE_BOARD_FILE_COUNT_EXCEEDED(2002, HttpStatus.BAD_REQUEST, "게시글당 최대 %d장까지 업로드할 수 있습니다."),
    ROOMMATE_BOARD_FILE_COUNT_THUMBNAIL_EXCEEDED(2003, HttpStatus.BAD_REQUEST, "썸네일은 최대 %d장 입니다."),
    ROOMMATE_BOARD_DECLARATION_DUPLICATE(2004, HttpStatus.BAD_REQUEST, "이미 신고한 룸메이트 게시글 입니다.");


    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
