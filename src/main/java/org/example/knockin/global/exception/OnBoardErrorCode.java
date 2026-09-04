package org.example.knockin.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OnBoardErrorCode implements ErrorCode{
    ONBOARD_BASIC_SAVE_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "기본정보 저장을 하지 못하였습니다."),
    ONBOARD_TERM_SAVE_ERROR(5001, HttpStatus.INTERNAL_SERVER_ERROR, "약관정보 저장을 하지 못하였습니다."),
    ONBOARD_LIFE_STYLE_SAVE_ERROR(5002, HttpStatus.INTERNAL_SERVER_ERROR, "생활패턴 저장을 하지 못하였습니다."),
    ONBOARD_LIFE_STYLE_NOT_FOUND(5003, HttpStatus.NOT_FOUND, "생활패턴을 찾지 못하였습니다."),
    ONBOARD_LIFE_STYLE_VAILDATION_FAIL(5004, HttpStatus.NOT_FOUND, "적절하지 않는 생활패턴을 값 입니다."),
    ONBOARD_ROOM_INFO_VAILDATION_FAIL(5005, HttpStatus.INTERNAL_SERVER_ERROR, "방 프로필 수정중 예상치 못한 에러가 발생하였습니다."),
    ONBOARD_PREFERENCE_STEP1_SAVE_ERROR(5006, HttpStatus.INTERNAL_SERVER_ERROR, "선호조건 Step1을 저장하지 못하였습니다."),
    ONBOARD_PREFERENCE_STEP1_LOG_SAVE_ERROR(5007, HttpStatus.INTERNAL_SERVER_ERROR, "선호조건 Step1 기록을 저장하지 못하였습니다."),
    ONBOARD_PREFERENCE_STEP2_SAVE_ERROR(5008, HttpStatus.INTERNAL_SERVER_ERROR, "선호조건 Step2을 저장하지 못하였습니다."),
    ONBOARD_PREFERENCE_STEP2_LOG_SAVE_ERROR(5009, HttpStatus.INTERNAL_SERVER_ERROR, "선호조건 Step2 기록을 저장하지 못하였습니다."),
    ONBOARD_PROFILE_STATE_CHANGE_ERROR(5010, HttpStatus.INTERNAL_SERVER_ERROR, "현재 매칭된 유저가 있어 프로필 상태 변경에 실패하였습니다."),
    ONBOARD_ROOM_INFO_SAVE_ERROR(5011, HttpStatus.INTERNAL_SERVER_ERROR, "방정보 저장을 하지 못하였습니다.");

    private final Integer no;
    private final HttpStatus httpStatus;
    private final String message;
}
