package org.example.knockin.mate.repository.row;

import java.time.LocalDateTime;

public record RepeatCalendarExcludeRow(
        Long repeatCalendarId,
        LocalDateTime excludeAt
) {
}
