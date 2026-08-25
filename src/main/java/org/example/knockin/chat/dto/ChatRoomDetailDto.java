package org.example.knockin.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.mate.dto.RoommateRequestDto.RoommateMatchingRequiredInfo;
import org.example.knockin.member.entity.Gender;

public class ChatRoomDetailDto {

    @Data
    public static class Request {

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "상대방 프로필 정보")
        private ProfileInfo opponentProfile;

        @Schema(description = "채팅 메세지 목록")
        private List<ChatMessage> messages;

        @Schema(description = "룸메이트 매칭 요청 목록")
        private List<RoommateMatchingRequiredInfo> matchingRequiredList;

        @Schema(description = "채팅 상대와의 양방향 차단 여부")
        private boolean blocked;

        @Schema(description = "상대방의 활성 룸메이트 여부")
        private boolean opponentHasRoommate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ProfileInfo {
        @Schema(description = "사용자 고유 식별 ID")
        private Long id;

        @Schema(description = "이름")
        private String name;

        @Schema(description = "나이")
        private Integer age;

        @Schema(description = "성별")
        private Gender gender;

        @Schema(description = "프로필 이미지 url")
        private String memberProfileImageUrl;

        @Schema(description = "점수 (TBD)")
        private Integer score;
    }

    @Data
    @AllArgsConstructor
    public static class ChatMessage {
        @Schema(description = "메세지 고유 식별 ID")
        private Long id;

        @Schema(description = "발송자 고유 식별 ID")
        private Long senderId;

        @Schema(description = "내용")
        private String contents;

        @Schema(description = "발송 시각")
        private LocalDateTime createdAt;

        @Schema(description = "메세지 타입")
        private MessageType type;

        @Schema(description = "이미지 URL")
        private String imageUrl;
    }
}
