package org.example.knockin.mate.repository.row;

import java.time.LocalDateTime;
import org.example.knockin.mate.entity.RepeatType;

public record MonthlyCalendarRow(
        Long calendarId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long repeatCalendarId,
        LocalDateTime repeatEndDate,
        RepeatType repeatType
) {
    public boolean isRepeat() {
        return repeatCalendarId != null;
    }
}
