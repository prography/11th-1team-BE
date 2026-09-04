package org.example.knockin.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.knockin.chat.entity.ChattingRequiredStatus;
import org.example.knockin.life.entity.LifePatternType;
import java.time.LocalDateTime;
import java.util.List;
import org.example.knockin.member.entity.Gender;

@Data
public class ChatRequestDetailDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "채팅 요청 고유 식별 ID")
        private Long requiredId;

        @Schema(description = "채팅 요청 상태")
        private ChattingRequiredStatus status;

        @Schema(description = "날짜 및 시간")
        private LocalDateTime createdAt;

        @Schema(description = "점수 (TBD)")
        private Integer score;

        @Schema(description = "내 정보")
        private MemberInfo me;

        @Schema(description = "상대방 정보")
        private MemberInfo opponent;

        @Schema(description = "현재 사용자의 요청자 여부")
        private Boolean isRequester;

        @Data
        @Builder
        @AllArgsConstructor
        public static class MemberInfo {
            @Schema(description = "사용자 고유 식별 ID")
            private Long memberId;

            @Schema(description = "이름")
            private String memberName;

            @Schema(description = "사용자 나이")
            private Integer memberAge;

            @Schema(description = "사용자 성별")
            private Gender gender;

            @Schema(description = "프로필 사진 URL")
            private String memberProfileImageUrl;

            @Schema(description = "라이프스타일 목록")
            private List<Lifestyle> lifeStyles;
        }

        @Data
        @Builder
        @AllArgsConstructor
        public static class Lifestyle {
            @Schema(description = "고유 식별 ID")
            private Long lifestyleId;

            @Schema(description = "이름")
            private String name;

            @Schema(description = "값")
            private String value;

            @Schema(description = "설명")
            private String description;

            @Schema(description = "타입/유형")
            private LifePatternType type;
        }
    }
}
