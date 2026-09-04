package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.knockin.mate.entity.RoommateRequiredStatus;

import java.time.LocalDateTime;

@Data
public class RoommateRequestListDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "룸메이트 요청 고유 식별 ID")
        private Long requiredId;

        @Schema(description = "요청자 ID")
        private Long requesterId;

        @Schema(description = "수신자 ID")
        private Long requesteeId;

        @Schema(description = "채팅방 ID")
        private Long chatRoomId;

        @Schema(description = "룸메이트 요청 상태")
        private RoommateRequiredStatus status;

        @Schema(description = "날짜 및 시간")
        private LocalDateTime createAt;
    }
}
