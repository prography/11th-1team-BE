package org.example.knockin.mate.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.mate.entity.ExcludeRoommateCalendar;
import org.example.knockin.mate.entity.RepeatRoommateCalendar;
import org.example.knockin.mate.repository.ExcludeRoommateCalendarRepository;
import org.example.knockin.mate.repository.row.RepeatCalendarExcludeRow;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExcludeRoommateCalendarServiceImpl {

    private final ExcludeRoommateCalendarRepository excludeRoommateCalendarRepository;

        public ExcludeRoommateCalendar save(RepeatRoommateCalendar repeatCalendar, LocalDateTime excludeAt) {
            ExcludeRoommateCalendar excludeCalendar = ExcludeRoommateCalendar.builder()
                    .repeatRoommateCalendar(repeatCalendar)
                    .excludeAt(excludeAt)
                    .build();
            return excludeRoommateCalendarRepository.save(excludeCalendar);
    }

    public List<RepeatCalendarExcludeRow> findRepeatCalendarExcludes(List<Long> repeatCalendarIds) {
        return excludeRoommateCalendarRepository.findRepeatCalendarExcludes(repeatCalendarIds);
    }
}
