package org.example.knockin.life.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SavePreferencesLifeStyleDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Schema(description = "lifestyles")
        private List<Long> lifestyles;
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "수정 일시")
        private LocalDateTime updatedAt;
    }
}