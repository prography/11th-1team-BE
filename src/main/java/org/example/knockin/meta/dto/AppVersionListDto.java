package org.example.knockin.meta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.meta.entity.PlatformType;
import org.example.knockin.meta.entity.UpdateType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppVersionListDto {
    @Data
    public static class Request {

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private List<VersionInfo> versionInfo;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class VersionInfo {
            @Schema(description = "고유 번호")
            private Long id;
            @Schema(description = "앱 버전")
            private String version;
            @Schema(description = "플랫폼")
            private PlatformType platformType;
            @Schema(description = "업데이트 유형")
            private UpdateType updateType;
            @Schema(description = "최소 지원")
            private String minVersion;
            @Schema(description = "출시일")
            private LocalDateTime createdAt;
        }
    }
}
