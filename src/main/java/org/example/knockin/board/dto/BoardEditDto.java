package org.example.knockin.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BoardEditDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Valid
        @Size(max = 10)
        @Schema(description = "이미지 목록")
        private List<BoardDetailDto.Response.FileDetailDto> images;

        @NotNull
        @Schema(description = "제목")
        private String title;

        @NotNull
        @Schema(description = "보증금")
        private int deposit;

        @NotNull
        @Schema(description = "월세")
        private int monthlyRent;

        @NotNull
        @Schema(description = "관리비")
        private int managementCost;

        @NotNull
        @Schema(description = "룸 형태")
        private RoomTypeInfo roomType;

        @NotNull
        @Schema(description = "지역")
        private RegionInfo region;

        @Schema(description = "입주 협의 가능 여부")
        private Boolean comeableDateNegotiable;

        @Schema(description = "입주 가능시기")
        private LocalDateTime comeableDate;

        @Schema(description = "방 추가 옵션 목록")
        private List<BoardDetailDto.Response.RoomExtraOptionInfo> roomExtraOptions;

        @NotNull
        @Schema(description = "내용")
        private String contents;

        @Schema(description = "생활패턴")
        private List<BoardDetailDto.Response.Lifestyle> lifeStyles;

        @Schema(description = "선호 룸메이트 조건 목록")
        private List<BoardDetailDto.Response.Condition> conditions;

        @Schema(description = "선호 룸메이트 중요 조건 목록")
        private List<BoardDetailDto.Response.ConditionWeight> conditionWeights;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RoomTypeInfo {
            @Schema(description = "고유 식별 ID")
            private Long roomTypeId;

            @Schema(description = "이름")
            private String name;

            @Schema(description = "이미지 URL")
            private String imageUrl;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RegionInfo {
            @Schema(description = "고유 식별 ID")
            private Long regionId;

            @Schema(description = "지역명")
            private String fullName;
        }

    }
}
