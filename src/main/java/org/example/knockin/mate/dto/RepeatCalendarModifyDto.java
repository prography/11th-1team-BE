package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RepeatCalendarModifyDto {

    @Data
    public static class Request {
        @Valid
        @Schema(description = "캘린더 정보")
        private CalendarDto.CalendarInfoDto calendar;

        @NotBlank
        @Size(min = 1, max = 50)
        @Schema(description = "카테고리명")
        private String categoryName;

        @Valid
        @NotNull
        @Schema(description = "반복 정보")
        private RepeatCalendarDto.RepeatCalendarInfo repeatInfo;

        @NotEmpty
        @Schema(description = "담당자 목록")
        private List<Long> memberIds;

        @NotNull
        @Schema(description = "수정 방식")
        private RepeatCalendarModifyType modifyType;

        @Valid
        @NotNull
        @Schema(description = "수정 대상 반복 일정의 원래 정보")
        private OriginalCalendar originalCalendar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "수정 일시")
        private LocalDateTime updatedAt;
    }

    @Data
    public static class OriginalCalendar {
        @NotNull
        @Schema(description = "수정 대상 반복 일정의 원래 시작일")
        private LocalDateTime startDate;

        @NotNull
        @Schema(description = "수정 대상 반복 일정의 원래 종료일")
        private LocalDateTime endDate;
    }
}
