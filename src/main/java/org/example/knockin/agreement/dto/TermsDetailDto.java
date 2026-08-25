package org.example.knockin.agreement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

@Data
public class TermsDetailDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "고유 식별 ID")
        private Long id;
        @Schema(description = "내용")
        private String contents;
    }
}