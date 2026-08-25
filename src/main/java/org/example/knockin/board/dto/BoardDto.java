package org.example.knockin.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@Data
public class BoardDto {
    @Data
    @Schema(name = "BoardSaveRequest")
    public static class Request {
        @NotNull
        @Schema(description = "제목")
        private String title;

        @NotNull
        @Schema(description = "내용")
        private String contents;

        @NotNull
        @Max(2000)
        @Schema(description = "보증금")
        private int deposit;

        @NotNull
        @Max(500)
        @Schema(description = "월세")
        private int mountlyRent;

        @NotNull
        @Schema(description = "관리비")
        private int managementCost;

        @NotNull
        @Schema(description = "방 타입 ID")
        private long roomTypeId;

        @NotNull
        @Schema(description = "지역 ID")
        private long regionId;

        @Schema(description = "입주 협의 가능 여부")
        private Boolean comeableDateNegotiable;

        @Schema(description = "입주 가능일")
        private LocalDateTime comeableDate;

        @Valid
        @Size(max = 10)
        @Schema(description = "이미지 목록 (이미지를 등록하는 경우 썸네일 이미지는 1개여야 합니다.)")
        private List<FileDto> images;

        @Schema(description = "방 추가옵션 고유 식별 ID")
        private List<Long> extraOptionIds;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(name = "BoardFileRequest")
        public static class FileDto {
            @NotNull
            @Schema(description = "files 파트에서 매칭할 파일 인덱스")
            private Integer fileIndex;

            @Schema(description = "썸네일 여부")
            private boolean thumbnail;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "BoardSaveResponse")
    public static class Response {
        @Schema(description = "날짜 및 시간")
        private LocalDateTime updatedAt;
    }
}
