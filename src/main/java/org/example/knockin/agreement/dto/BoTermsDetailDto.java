package org.example.knockin.agreement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class BoTermsDetailDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "고유 식별 ID")
        private Long id;
        @Schema(description = "제목")
        private String title;
        @Schema(description = "내용")
        private String contents;
        @Schema(description = "생성 일시")
        private LocalDateTime createAt;
    }
}