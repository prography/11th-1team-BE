package org.example.knockin.meta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FaqAllListDto {
    @Data
    public static class Request {

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private List<FaqInfo> faqInfoList;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class FaqInfo {
            @Schema(description = "고유 번호")
            private Long id;
            @Schema(description = "제목")
            private String title;
            @Schema(description = "내용")
            private String contents;
            @Schema(description = "정렬 순서")
            private Integer sort;
            @Schema(description = "등록일")
            private LocalDateTime createAt;
            @Schema(description = "수정일")
            private LocalDateTime updatedAt;
        }
    }
}
