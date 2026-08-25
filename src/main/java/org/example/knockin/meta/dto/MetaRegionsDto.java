package org.example.knockin.meta.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
public class MetaRegionsDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "region")
        private List<RegionItem> region;

        @Data
        @Builder
        public static class RegionItem {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "이름")
            private String name;
            @Schema(description = "parent id")
            private Long parentId;
        }
    }
}