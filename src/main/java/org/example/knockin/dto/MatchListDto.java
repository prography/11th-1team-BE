package org.example.knockin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.authentication.entity.AuthenticationType;
import org.example.knockin.life.entity.LifePatternType;
import org.example.knockin.member.entity.Gender;
import org.example.knockin.room.entity.RoomProfileType;
import java.util.List;

@Data
public class MatchListDto {
    @Data
    public static class Request {
        @Schema(description = "조회 크기")
        @Min(1)
        @Max(50)
        private Integer size = 20;

        @Schema(description = "이미 조회한 회원 ID 목록")
        private List<Long> excludeMemberIds = new ArrayList<>();

        @Schema(description = "관심 회원만 조회")
        private Boolean likedOnly;
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
        private SeekerProfile seekerProfile;

        @Schema(description = "방 있어요 프로필 (OFFER)")
        private OfferProfile offerProfile;

        @Schema(description = "점수 (TBD)")
        private Integer score;

        @Schema(description = "라이프스타일 목록")
        private List<Lifestyle> lifeStyles;

        @Schema(description = "조건 목록")
        private List<Condition> conditions;

        @Schema(description = "중요 조건 목록")
        private List<ConditionWeight> conditionWeights;

        @Schema(description = "승인된 신원 인증")
        private List<AuthenticationType> authentications;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeekerProfile {
        @Schema(description = "최소 보증금")
        private Integer minDeposit;

        @Schema(description = "최대 보증금")
        private Integer maxDeposit;

        @Schema(description = "최소 월세")
        private Integer minMonthlyRent;

        @Schema(description = "최대 월세")
        private Integer maxMonthlyRent;

        @Schema(description = "원하는 방 타입명 목록")
        private List<String> roomTypeNames;

        @Schema(description = "원하는 지역명 목록")
        private List<String> regionFullNames;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferProfile {
        @Schema(description = "보증금")
        private Integer deposit;

        @Schema(description = "월세")
        private Integer monthlyRent;

        @Schema(description = "지역명")
        private String regionFullName;

        @Schema(description = "방 타입명")
        private String roomTypeName;
    }

    @Data
    @Builder
    @NoArgsConstructor
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
        @Schema(description = "이미지 URL")
        private String imageUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Condition {
        @Schema(description = "고유 식별 ID")
        private Long conditionId;
        @Schema(description = "이름")
        private String name;
        @Schema(description = "값")
        private String value;
        @Schema(description = "설명")
        private String description;
        @Schema(description = "타입/유형")
        private LifePatternType type;
        @Schema(description = "이미지 URL")
        private String imageUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConditionWeight {
        @Schema(description = "고유 식별 ID")
        private Long conditionWeightId;
        @Schema(description = "이름")
        private String name;
        @Schema(description = "이미지 URL")
        private String imageUrl;
    }
}
