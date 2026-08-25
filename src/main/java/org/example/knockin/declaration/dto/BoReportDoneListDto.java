package org.example.knockin.declaration.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.declaration.entity.DeclarationType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoReportDoneListDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "회원 목록")
        private List<ReportInfo> reportInfoList;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ReportInfo {
            @Schema(description = "고유 번호")
            private Long id;
            @Schema(description = "신고 유형")
            private String type;
            @Schema(description = "신고자")
            private String reporter;
            @Schema(description = "처리결과")
            private DeclarationType declarationType;
            @Schema(description = "처리일")
            private LocalDateTime createdAt;
        }
    }
}