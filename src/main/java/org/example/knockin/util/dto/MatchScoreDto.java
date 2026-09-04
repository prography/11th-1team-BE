package org.example.knockin.util.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class MatchScoreDto {
    @Data
    public static class Request {
    }

    @Data
    public static class Response {
        @Schema(description = "compatibility")
        private Compatibility compatibility;
    }
}
