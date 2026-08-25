package org.example.knockin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.member.entity.DevicePlatform;

@Data
public class FcmDto {

    @Data
    public static class Request {
        @Schema(description = "앱 설치 시 생성 후 SecureStore에 보관한 UUID")
        @NotBlank
        @Size(max = 50)
        private String deviceId;

        @Schema(description = "Firebase Messaging이 생성한 실제 토큰")
        @NotBlank
        @Size(max = 512)
        private String fcmToken;

        @Schema(description = "ANDROID / IOS")
        @NotNull
        private DevicePlatform platform;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "수정 일시")
        private LocalDateTime updatedAt;
    }
}
