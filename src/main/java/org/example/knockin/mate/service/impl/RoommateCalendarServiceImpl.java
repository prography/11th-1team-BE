package org.example.knockin.mate.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.mate.dto.CalendarDto.CalendarInfoDto;
import org.example.knockin.member.entity.Member;
import org.example.knockin.mate.entity.MyRoommate;
import org.example.knockin.mate.entity.RoommateCalendar;
import org.example.knockin.mate.entity.RoommateCalendarCategory;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.MyRoommateErrorCode;
import org.example.knockin.mate.repository.RoommateCalendarRepository;
import org.example.knockin.mate.repository.row.DailyCalendarRow;
import org.example.knockin.mate.repository.row.MonthlyCalendarRow;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoommateCalendarServiceImpl {

    private final RoommateCalendarRepository roommateCalendarRepository;

    public RoommateCalendar save(MyRoommate myRoommate, Member member, RoommateCalendarCategory category, CalendarInfoDto dto) {
        RoommateCalendar roommateCalendar = RoommateCalendar.builder()
                .myRoommate(myRoommate)
                .member(member)
                .roommateCalendarCategory(category)
                .title(dto.getTitle())
                .contents(dto.getContents())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        return roommateCalendarRepository.save(roommateCalendar);
    }

    public RoommateCalendar findByIdOrThrow(Long calendarId) {
        return roommateCalendarRepository.findById(calendarId)
                .orElseThrow(() -> new BusinessException(MyRoommateErrorCode.CALENDER_NOT_FOUND));
    }

    public List<DailyCalendarRow> findDailyCalendarList(Long myRoommateId, LocalDateTime from, LocalDateTime to) {
        return roommateCalendarRepository.findDailyCalendarList(myRoommateId, from, to);
    }

    public List<MonthlyCalendarRow> findMonthlyCalendarList(Long myRoommateId, LocalDateTime from, LocalDateTime to) {
        return roommateCalendarRepository.findMonthlyCalendarList(myRoommateId, from, to);
    }
}
