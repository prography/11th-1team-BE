package org.example.knockin.inquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoInquiryListDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @Schema(description = "회원명 및 문의 내용")
        private String searchKeyword;
        @Schema(description = "상태")
        private Boolean isReply;
        @Schema(description = "유형")
        private Long categoryId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "문의 목록")
        private List<InquiryItem> inquiries;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class InquiryItem {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "제목")
            private String title;
            @Schema(description = "작성자")
            private String writer;
            @Schema(description = "상태")
            private String status;
            @Schema(description = "날짜 및 시간")
            private LocalDateTime createAt;
            @Schema(description = "타입/유형")
            private String type;
        }
    }
}
