package org.example.knockin.life.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.example.knockin.life.entity.LifePatternType;
import java.util.List;

@Data
public class MyPreferencesAllDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "라이프스타일 목록")
        private List<Lifestyle> lifestyles;
        @Schema(description = "조건 목록")
        private List<Condition> conditions;

        @Data
        public static class Lifestyle {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "생활패턴 고유 식별 ID")
            private Long lifestyleId;
            @Schema(description = "이름")
            private String name;
            @Schema(description = "값")
            private String value;
            @Schema(description = "설명")
            private String description;
            @Schema(description = "타입/유형")
            private LifePatternType type;
        }

        @Data
        public static class Condition {
            @Schema(description = "고유 식별 ID")
            private Long conditionsId;
            @Schema(description = "이름")
            private String name;
        }
    }
}
