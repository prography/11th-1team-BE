package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class MyRoommateCalendarDetailDto {
    @Data
    public static class Request {
    }

    @Data
    public static class Response {
        @Schema(description = "고유 식별 ID")
        private Long id;
        @Schema(description = "제목")
        private String title;
        @Schema(description = "내용")
        private String contents;
    }
}