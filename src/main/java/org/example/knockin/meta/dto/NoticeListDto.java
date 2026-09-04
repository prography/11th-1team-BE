package org.example.knockin.meta.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeListDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "공지사항 목록")
        private List<NoticeItem> notices;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class NoticeItem {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "제목")
            private String title;
            @Schema(description = "작성자")
            private String writer;
            @Schema(description = "날짜 및 시간")
            private LocalDateTime createAt;
        }
    }
}
