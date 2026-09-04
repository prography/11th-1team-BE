package org.example.knockin.global.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum InquiryAlarmTemplate implements AlarmMessageTemplate {
    INQUIRIE_REPLY("문의 답변이 도착했어요", "고객센터에 답변이 등록되었습니다. 확인해보세요.", "knockinrn://mypage"),
    ;

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
