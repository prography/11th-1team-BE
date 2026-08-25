package org.example.knockin.life.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.life.entity.LifePatternType;
import java.util.List;

@Data
public class BoLifeStylePatternDetailDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "고유 식별 ID")
        private Long id;
        @Schema(description = "이름")
        private String name;
        @Schema(description = "이미지")
        private String image;
        @Schema(description = "유형")
        private LifePatternType type;
        @Schema(description = "생활패턴 설명")
        private String lifePatternDescription;
        @Schema(description = "선호조건 설명")
        private String preferenceDescription;
        @Schema(description = "순서")
        private Integer sort;
        @Schema(description = "details")
        private List<DetailItem> details;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class DetailItem {
            @Schema(description = "values")
            private String values;
            @Schema(description = "설명")
            private String description;
        }
    }
}