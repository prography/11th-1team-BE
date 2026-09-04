package org.example.knockin.global.entity;

import lombok.RequiredArgsConstructor;
import org.example.knockin.mate.entity.RoommateRequiredStatus;

@RequiredArgsConstructor
public enum RoommateRequiredMessageTemplate implements AlarmMessageTemplate {
    PENDING("%s님의 매칭 요청", "%s님이 매칭을 요청했어요.", "knockinrn://chat/%s"),
    ACCEPTED("매칭 요청이 수락됐어요", "%s님과의 룸메이트가 이뤄졌어요.", "knockinrn://chat/%s"),
    REJECTED("매칭 요청 거절", "%s님이 매칭 요청을 거절했어요.", "knockinrn://chat/%s"),
    ;

    private final String title;
    private final String contents;
    private final String deepLink;

    public String formatTitle(Object... args) {
        return String.format(this.title, args);
    }

    public String formatContents(Object... args) {
        return String.format(this.contents, args);
    }

    public String formatDeepLink(Object... args) {
        return String.format(this.deepLink, args);
    }

    public static RoommateRequiredMessageTemplate of(RoommateRequiredStatus status) {
        return switch (status) {
            case PENDING -> RoommateRequiredMessageTemplate.PENDING;
            case ACCEPTED -> RoommateRequiredMessageTemplate.ACCEPTED;
            case REJECTED -> RoommateRequiredMessageTemplate.REJECTED;
            case CANCELED, EXPIRED -> throw new IllegalArgumentException("지원하지 않는 룸메이트 매칭 상태값입니다.");
        };
    }
}
