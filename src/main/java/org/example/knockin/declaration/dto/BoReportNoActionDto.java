package org.example.knockin.declaration.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.global.util.ReportType;

import java.time.LocalDateTime;

@Data
public class BoReportNoActionDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @Schema(description = "고유 번혼")
        private Long id;
        @Schema(description = "유형")
        private ReportType type;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "수정 일시")
        private LocalDateTime updatedAt;
    }
}