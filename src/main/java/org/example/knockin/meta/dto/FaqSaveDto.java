package org.example.knockin.meta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class FaqSaveDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Schema(description = "제목")
        private String title;
        @Schema(description = "내용")
        private String contents;
        @Schema(description = "정렬 순서")
        private Integer sort;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "저장 일시")
        private LocalDateTime updatedAt;
    }
}
