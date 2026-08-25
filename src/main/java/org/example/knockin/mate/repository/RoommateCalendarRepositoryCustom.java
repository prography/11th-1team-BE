package org.example.knockin.mate.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.example.knockin.mate.repository.row.DailyCalendarRow;
import org.example.knockin.mate.repository.row.MonthlyCalendarRow;

public interface RoommateCalendarRepositoryCustom {
    List<DailyCalendarRow> findDailyCalendarList(Long myRoommateId, LocalDateTime from, LocalDateTime to);

    List<MonthlyCalendarRow> findMonthlyCalendarList(Long myRoommateId, LocalDateTime from, LocalDateTime to);
}
