package org.example.knockin.mate.repository;

import java.util.List;
import org.example.knockin.mate.repository.row.RepeatCalendarExcludeRow;

public interface ExcludeRoommateCalendarRepositoryCustom {
    List<RepeatCalendarExcludeRow> findRepeatCalendarExcludes(List<Long> repeatCalendarIds);
}
