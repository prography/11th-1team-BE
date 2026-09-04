package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class MyRoommateDto {

    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "날짜 및 시간")
        private LocalDateTime updatedAt;
    }
}
