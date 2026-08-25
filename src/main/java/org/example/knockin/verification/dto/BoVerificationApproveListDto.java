package org.example.knockin.verification.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.verification.entity.AuthenticationType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoVerificationApproveListDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "employee auth")
        private List<EmployeeAuthItem> employeeAuth;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class EmployeeAuthItem {
            @Schema(description = "고유 번호")
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
            @Schema(description = "처리자")
            private String accepter;
        }
    }
}