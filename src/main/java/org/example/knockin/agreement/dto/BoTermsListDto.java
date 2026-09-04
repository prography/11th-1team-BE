package org.example.knockin.agreement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoTermsListDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private Long agreementTypeId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "약관")
        private List<TermsItem> terms;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class TermsItem {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "제목")
            private String title;
            @Schema(description = "생성 일시")
            private LocalDateTime createAt;
            @Schema(description = "현행 여부")
            private boolean isCurrent;
        }
    }
}