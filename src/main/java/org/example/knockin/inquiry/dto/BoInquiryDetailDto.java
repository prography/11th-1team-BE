package org.example.knockin.inquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoInquiryDetailDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "문의 상세 정보")
        private InquiryDetail inquirie;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class InquiryDetail {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "제목")
            private String title;
            @Schema(description = "내용")
            private String contents;
            @Schema(description = "작성자")
            private String writer;
            @Schema(description = "상태")
            private String status;
            @Schema(description = "날짜 및 시간")
            private LocalDateTime createAt;
            @Schema(description = "타입/유형")
            private String type;
            @Schema(description = "답변 목록")
            private List<Reply> reply;

            @Data
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Reply {
                @Schema(description = "고유 식별 ID")
                private Long id;
                @Schema(description = "내용")
                private String contents;
                @Schema(description = "작성자")
                private String writer;
                @Schema(description = "날짜 및 시간")
                private LocalDateTime createAt;
            }
        }
    }
}
