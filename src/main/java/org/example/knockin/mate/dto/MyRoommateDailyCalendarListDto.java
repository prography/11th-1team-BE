package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import org.example.knockin.mate.entity.RepeatType;
import org.jspecify.annotations.Nullable;

@Data
public class MyRoommateDailyCalendarListDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "조회한 날짜")
        private LocalDate targetDay;

        @Schema(description = "일정 목록")
        private List<CalendarItem> calendars;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class CalendarItem {
        @Schema(description = "캘린더 기본 정보")
        private CalendarBasicInfo calendarBasicInfo;

        @Schema(description = "담당자 목록")
        private List<CalendarMember> calendarMembers;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class CalendarBasicInfo {
        @NotNull
        @Schema(description = "고유 식별 ID")
        private Long calendarId;

        @NotBlank
        @Size(min = 1, max = 50)
        @Schema(description = "일정 제목")
        private String title;

        @NotBlank
        @Size(min = 1, max = 500)
        @Schema(description = "일정 내용")
        private String contents;

        @NotNull
        @Schema(description = "하루 종일 여부")
        private Boolean isAllDay;

        @NotNull
        @Schema(description = "일정 시작일")
        private LocalDateTime startDate;

        @NotNull
        @Schema(description = "일정 종료일 (당일치기면 일정 시작일과 동일)")
        private LocalDateTime endDate;

        @NotNull
        @Schema(description = "카테고리명")
        private String categoryName;

        @Nullable
        @Schema(description = "반복 타입 (단일 일정은 null 입니다)")
        private RepeatType repeatType;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class CalendarMember {
        @NotNull
        @Schema(description = "고유 식별 ID")
        private Long memberId;

        @Schema(description = "사용자 이름")
        private String name;
    }
}
