package org.example.knockin.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.verification.entity.AuthenticationType;
import org.example.knockin.mate.entity.RoommateRequiredStatus;

@Data
public class ChatRoomListDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "채팅방 식별 고유 ID")
        private Long chatRoomId;

        @Schema(description = "상대방 이름")
        private String memberName;

        @Schema(description = "상대방 프로필 사진 URL")
        private String memberProfileImageUrl;

        @Schema(description = "채팅방 생성 일자")
        private LocalDateTime createdAt;

        @Schema(description = "룸메이트 요청 상태")
        private RoommateRequiredStatus roommateStatus;

        @Schema(description = "룸메이트 여부")
        private Boolean isRoommate;

        @Schema(description = "신원 인증 타입 목록")
        private List<AuthenticationType> authenticationTypes;
        
        @Schema(description = "마지막 메세지")
        private String lastMessage;

        @Schema(description = "안 읽은 메시지 수")
        private Integer messageCount;

        @Schema(description = "마지막 메세지 도착 시간")
        private LocalDateTime lastMessageAt;
    }
}
