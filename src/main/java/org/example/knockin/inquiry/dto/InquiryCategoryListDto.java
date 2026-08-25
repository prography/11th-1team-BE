package org.example.knockin.inquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
public class InquiryCategoryListDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "inquirie categorys")
        private List<Category> inquirieCategorys;

        @Data
        @Builder
        public static class Category {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "이름")
            private String name;
        }
    }
}