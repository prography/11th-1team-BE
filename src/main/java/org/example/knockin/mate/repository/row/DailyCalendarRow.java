package org.example.knockin.mate.repository.row;

import java.time.LocalDateTime;
import org.example.knockin.mate.entity.RepeatType;

public record DailyCalendarRow(
        Long calendarId,
        String title,
        String contents,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String categoryName,
        Long repeatCalendarId,
        LocalDateTime repeatEndDate,
        RepeatType repeatType
) {
    public boolean isRepeat() {
        return repeatCalendarId != null;
    }
}
