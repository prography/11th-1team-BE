package org.example.knockin.mate.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.mate.dto.RepeatCalendarDto.RepeatCalendarInfo;
import org.example.knockin.mate.entity.RepeatRoommateCalendar;
import org.example.knockin.mate.entity.RoommateCalendar;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.MyRoommateErrorCode;
import org.example.knockin.mate.repository.RepeatRoommateCalendarRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepeatRoommateCalendarServiceImpl {

    private final RepeatRoommateCalendarRepository repeatRoommateCalendarRepository;

    public RepeatRoommateCalendar save(RoommateCalendar calendar, RepeatCalendarInfo repeatInfo) {
        RepeatRoommateCalendar repeatRoommateCalendar = RepeatRoommateCalendar.builder()
                .roommateCalendar(calendar)
                .endDate(repeatInfo.getEndDate())
                .repeatType(repeatInfo.getRepeatType())
                .build();
        return repeatRoommateCalendarRepository.save(repeatRoommateCalendar);
    }

    public RepeatRoommateCalendar findOneByRoommateCalendarOrThrow(RoommateCalendar calendar) {
        return repeatRoommateCalendarRepository.findOneByRoommateCalendar(calendar)
                .orElseThrow(() -> new BusinessException(MyRoommateErrorCode.CALENDER_NOT_REPEAT));
    }
}
