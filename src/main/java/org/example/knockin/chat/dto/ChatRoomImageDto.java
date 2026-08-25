package org.example.knockin.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ChatRoomImageDto {
    @Data
    public static class Request {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "이미지 URL")
        private String imageUrl;
    }
}
