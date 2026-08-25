package org.example.knockin.mate.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class HouseRuleDto {
    @Data
    public static class Request {
        @NotBlank
        @Size(min = 1, max = 30)
        @Schema(description = "제목")
        private String title;

        @NotBlank
        @Size(min = 1, max = 50)
        @Schema(description = "내용")
        private String contents;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "날짜 및 시간")
        private LocalDateTime updatedAt;
    }
}
