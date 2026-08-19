package org.example.knockin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.entity.member.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaveProfileBasicDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @Schema(description = "birth")
        @NotNull
        @Past
        private LocalDate birth;
        @Schema(description = "gender")
        @NotNull
        private Gender gender;
        @Schema(description = "약관")
        @NotEmpty
        private List<Long> terms;
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "수정 일시")
        private LocalDateTime updatedAt;
    }
}