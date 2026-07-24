package org.example.knockin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
public class ChatRequestDto {
    @Data
    public static class Request {
        @NotNull
        @Schema(description = "채팅 피요청자 멤버 고유 식별 ID")
        private Long requesteeId;
        @Schema(description = "게시글 고유 식별 ID")
        private Long boardId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long chatRoomId;
        private LocalDateTime updatedAt;

        public Response(LocalDateTime updatedAt) {
            this(null, updatedAt);
        }
    }
}
