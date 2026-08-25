package org.example.knockin.inquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class InquiryDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Schema(description = "카테고리 ID")
        private Long categoryId;
        @Schema(description = "제목")
        private String title;
        @Schema(description = "내용")
        private String contents;
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "수정 일시")
        private LocalDateTime updatedAt;
    }
}