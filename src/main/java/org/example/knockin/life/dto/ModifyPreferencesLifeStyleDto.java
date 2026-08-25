package org.example.knockin.life.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ModifyPreferencesLifeStyleDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Schema(description = "lifestyles")
        private List<LifeStyleInfo> lifestyles;

        @Data
        public static class LifeStyleInfo {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "라이프스타일 id")
            private Long lifestyleId;
        }
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "수정 일시")
        private LocalDateTime updatedAt;
    }
}