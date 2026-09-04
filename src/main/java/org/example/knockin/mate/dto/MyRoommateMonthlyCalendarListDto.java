package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MyRoommateMonthlyCalendarListDto {

    @Data
    public static class Request {

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        @Schema(description = "조회한 날짜 (년/월)")
        private YearMonth targetMonth;

        @Schema(description = "날짜 정보 목록")
        private List<CalendarDay> calendarDays;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalendarDay {
        @Schema(description = "조회한 날짜 (년/월/일)")
        private LocalDate targetDate;

        @Schema(description = "존재 여부")
        private Boolean exists;
    }
}
