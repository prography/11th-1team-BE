package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
public class CalendarCategoryDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "카테고리명 목록")
        private List<String> categoryNames;
    }
}