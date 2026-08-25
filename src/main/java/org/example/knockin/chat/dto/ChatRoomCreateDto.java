package org.example.knockin.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class ChatRoomCreateDto {
    @Data
    public static class Request {
        @NotNull
        @Schema(description = "채팅 피요청자 멤버 고유 식별 ID")
        private Long requesteeId;

        @Schema(description = "게시글 고유 식별 ID")
        private Long boardId;

        @Valid
        @NotNull
        @Schema(description = "첫 채팅 메세지")
        private ChatMessage chatMessage;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {

        @Schema(description = "채팅방 고유 식별 ID")
        private Long chatRoomId;

        @Schema(description = "수정 일시")
        private LocalDateTime updatedAt;
    }

    @Data
    public static class ChatMessage {
        @NotBlank
        @Schema(description = "메세지 내용")
        private String contents;
    }
}
