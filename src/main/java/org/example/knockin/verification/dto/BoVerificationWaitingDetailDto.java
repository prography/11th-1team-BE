package org.example.knockin.verification.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import org.example.knockin.verification.entity.AuthenticationType;

import java.time.LocalDateTime;

@Data
public class BoVerificationWaitingDetailDto {
    @Data
    public static class Request {
    }

    @Data
    public static class Response {
        @Schema(description = "회원 번호")
        private Long id;
        @Schema(description = "이름")
        private String name;
        @Schema(description = "유형")
        private AuthenticationType type;
        @Schema(description = "수락 여부")
        private Boolean isAccepted;
        @Schema(description = "이메일")
        private String email;
        @Schema(description = "생성 일시")
        private LocalDateTime createAt;
        @Schema(description = "경과일")
        private Integer elapsedAt;
    }
}