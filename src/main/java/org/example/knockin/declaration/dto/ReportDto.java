package org.example.knockin.declaration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDto {
    @Data
    public static class Request {

        @NotBlank
        @Size(max = 500)
        @Schema(description = "신고 사유")
        private String contents;
    }

    @Data
    @AllArgsConstructor
    public static class Response {
        private LocalDateTime updatedAt;
    }
}
