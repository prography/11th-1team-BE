package org.example.knockin.mate.repository;

import java.util.List;
import org.example.knockin.mate.repository.row.DailyCalendarMemberRow;

public interface RoommateCalendarMemberRepositoryCustom {
    List<DailyCalendarMemberRow> findDailyCalendarMembers(List<Long> calendarIds);
}
