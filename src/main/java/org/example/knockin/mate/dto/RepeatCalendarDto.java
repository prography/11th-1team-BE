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
import org.example.knockin.mate.entity.RepeatType;
import org.jspecify.annotations.Nullable;

public class RepeatCalendarDto {
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
        @Schema(description = "반복 정보")
        private RepeatCalendarInfo repeatInfo;

        @NotEmpty
        @Schema(description = "담당자 목록")
        private List<Long> memberIds;
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RepeatCalendarInfo {
        @Nullable
        @Schema(description = "종료 일자")
        private LocalDateTime endDate;

        @NotNull
        @Schema(description = "반복 유형")
        private RepeatType repeatType;
    }
}
