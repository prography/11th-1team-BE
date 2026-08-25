package org.example.knockin.declaration.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoReportWaitListDto {
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
            @Schema(description = "신고자 고유번호")
            private Long reporterId;
            @Schema(description = "피신고 고유번호")
            private Long reportedId;
            @Schema(description = "신고일")
            private LocalDateTime createdAt;
            @Schema(description = "신고사유")
            private String reason;
        }
    }
}