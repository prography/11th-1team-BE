package org.example.knockin.matching.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compatibility {
    @Schema(description = "총 점수")
    private Integer totalScore;

    @Schema(description = "라이프스타일 정보 목록")
    private List<LifeStyleInfo> lifeStyleInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LifeStyleInfo {
        @Schema(description = "고유 식별 ID")
        private Long id;

        @Schema(description = "생활패턴 이름")
        private String name;

        @Schema(description = "백분율")
        private Integer percent;
    }
}
