package org.example.knockin.meta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.verification.entity.AuthenticationType;

import java.util.List;

@Data
public class AuthEmailListDto {
    @Data
    public static class Request {

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private List<AuthEmailInfo> authEmailInfoList;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AuthEmailInfo {
            @Schema(description = "고유 번호")
            private Long id;
            @Schema(description = "도메인")
            private String domain;
            @Schema(description = "이름")
            private String name;
            @Schema(description = "유형")
            private AuthenticationType type;
        }
    }
}
