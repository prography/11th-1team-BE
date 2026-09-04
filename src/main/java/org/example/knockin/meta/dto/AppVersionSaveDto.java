package org.example.knockin.meta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.meta.entity.PlatformType;
import org.example.knockin.meta.entity.UpdateType;

import java.time.LocalDateTime;

@Data
public class AppVersionSaveDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Schema(description = "앱 버전")
        private String version;
        @Schema(description = "플랫폼")
        private PlatformType platformType;
        @Schema(description = "업데이트 유형")
        private UpdateType updateType;
        @Schema(description = "최소 지원")
        private String minVersion;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "삽입 일시")
        private LocalDateTime updatedAt;
    }
}
