package org.example.knockin.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class EmailConfirmDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Schema(description = "이메일")
        private String email;
        @Schema(description = "인증 번호")
        private String authNo;
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