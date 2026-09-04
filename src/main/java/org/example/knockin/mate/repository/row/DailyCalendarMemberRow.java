package org.example.knockin.mate.repository.row;

public record DailyCalendarMemberRow(
        Long calendarId,
        Long memberId,
        String name
) {
}
