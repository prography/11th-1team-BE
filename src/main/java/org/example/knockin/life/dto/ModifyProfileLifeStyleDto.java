package org.example.knockin.life.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ModifyProfileLifeStyleDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Schema(description = "lifestyles")
        @Valid
        @NotEmpty
        private List<LifeStyleInfo> lifestyles;

        @Data
        public static class LifeStyleInfo {
            @Schema(description = "고유 식별 ID")
            @NotNull
            private Long id;
            @Schema(description = "라이프스타일 id")
            @NotNull
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