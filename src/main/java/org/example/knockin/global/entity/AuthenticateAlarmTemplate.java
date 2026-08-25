package org.example.knockin.global.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthenticateAlarmTemplate implements AlarmMessageTemplate {
    SAVE_VERIFICATION("인증 완료!", "신분 인증이 완료되었습니다.", "knockinrn://mypage"),
    DELETE_VERIFICATION("인증 반려 안내", "신분 인증이 반려되었습니다. 사유: %s", "knockinrn://mypage");

    private final String title;
    private final String contents;
    private final String deepLink;

    @Override
    public String formatTitle(Object... args) {
        return String.format(this.title, args);
    }

    @Override
    public String formatContents(Object... args) {
        return String.format(this.contents, args);
    }

    @Override
    public String formatDeepLink(Object... args) {
        return String.format(this.deepLink, args);
    }
}
