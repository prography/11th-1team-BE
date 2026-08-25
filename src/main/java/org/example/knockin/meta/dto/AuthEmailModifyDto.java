package org.example.knockin.meta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.verification.entity.AuthenticationType;

import java.time.LocalDateTime;

@Data
public class AuthEmailModifyDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Schema(description = "고유 번호")
        private Long id;
        @Schema(description = "도메인")
        private String domain;
        @Schema(description = "이름")
        private String name;
        @Schema(description = "유형")
        private AuthenticationType type;
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
