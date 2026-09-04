package org.example.knockin.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import org.example.knockin.chat.entity.ChattingRequiredStatus;
import org.example.knockin.member.entity.Gender;

@Data
public class ChatRequestListDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "채팅 요청 고유 식별 ID")
        private Long requiredId;

        @Schema(description = "채팅 요청 상태값")
        private ChattingRequiredStatus status;

        @Schema(description = "사용자 고유 식별 ID")
        private Long memberId;

        @Schema(description = "이름")
        private String memberName;

        @Schema(description = "사용자 나이")
        private Integer memberAge;

        @Schema(description = "사용자 성별")
        private Gender gender;

        @Schema(description = "궁합 점수")
        private Integer score;

        @Schema(description = "생성일자")
        private LocalDateTime createdAt;
    }
}