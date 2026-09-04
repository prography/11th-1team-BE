package org.example.knockin.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.member.entity.Gender;

@Data
public class MyRoommateCardDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "내 룸메 고유 식별 ID")
        private Long id;

        @Schema(description = "내 룸메 정보")
        private MyRoommateInfo myRoommateInfo;

        @Schema(description = "채팅방 고유 식별 ID")
        private Long chatRoomId;

        @Schema(description = "궁합 점수")
        private Integer score;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MyRoommateInfo {
            @Schema(description = "내 룸메 회원 고유 식별 ID")
            private Long memberId;

            @Schema(description = "내 룸메 이름")
            private String memberName;

            @Schema(description = "내 룸메 나이")
            private Integer memberAge;

            @Schema(description = "내 룸메 성별")
            private Gender gender;

            @Schema(description = "내 룸메 프로필 사진 URL")
            private String memberProfileImageUrl;
        }
    }


}
