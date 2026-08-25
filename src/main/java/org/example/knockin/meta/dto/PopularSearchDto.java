package org.example.knockin.meta.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
public class PopularSearchDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "rank")
        private List<RankItem> rank;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RankItem {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "keyword")
            private String keyword;
        }
    }
}