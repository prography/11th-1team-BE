package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class HouseRuleDetailDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "고유 식별 ID")
        private Long id;

        @Schema(description = "하우스 룰 제목")
        private String title;

        @Schema(description = "하우스 룰 본문")
        private String contents;
    }
}
