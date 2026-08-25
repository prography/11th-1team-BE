package org.example.knockin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.authentication.entity.AuthenticationType;
import org.example.knockin.member.entity.Gender;
import org.example.knockin.room.entity.RoomProfileType;

@Data
public class MatchDetailDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "고유 식별 ID")
        private Long memberId;

        @Schema(description = "프로필 사진 URL")
        private String memberProfileImageUrl;

        @Schema(description = "이름")
        private String memberName;

        @Schema(description = "나이")
        private Integer memberAge;

        @Schema(description = "성별")
        private Gender gender;

        @Schema(description = "좋아요 여부")
        private Boolean interested;

        @Schema(description = "방 프로필 타입")
        private RoomProfileType roomProfileType;

        @Schema(description = "방 없어요 프로필 (SEEKER)")
        private MatchListDto.SeekerProfile seekerProfile;

        @Schema(description = "방 있어요 프로필 (OFFER)")
        private MatchListDto.OfferProfile offerProfile;

        @Schema(description = "라이프스타일 목록")
        private List<MatchListDto.Lifestyle> lifeStyles;

        @Schema(description = "조건 목록")
        private List<MatchListDto.Condition> conditions;

        @Schema(description = "중요 조건 목록")
        private List<MatchListDto.ConditionWeight> conditionWeights;

        @Schema(description = "승인된 신원 인증")
        private List<AuthenticationType> authentications;

        @Schema(description = "적합도 (TBD)")
        private Compatibility compatibility;

        @Schema(description = "본인 여부")
        private boolean mine;
    }
}
