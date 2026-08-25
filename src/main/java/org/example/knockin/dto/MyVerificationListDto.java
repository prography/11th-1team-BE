package org.example.knockin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.authentication.entity.ApproveType;

import java.time.LocalDateTime;

@Data
public class MyVerificationListDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "학생 auth")
        private AuthInfo studentAuth;
        @Schema(description = "회사 auth")
        private AuthInfo employeeAuth;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AuthInfo {
            @Schema(description = "수락 여부")
            private ApproveType status;
            @Schema(description = "이메일")
            private String email;
            @Schema(description = "생성 일시")
            private LocalDateTime createAt;
        }
    }
}