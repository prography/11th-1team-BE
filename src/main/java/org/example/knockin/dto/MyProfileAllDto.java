package org.example.knockin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.example.knockin.entity.life.LifePatternType;
import org.example.knockin.entity.member.Gender;
import org.example.knockin.entity.member.MemberPrivacyType;
import org.example.knockin.entity.room.RoomProfileType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MyProfileAllDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    public static class Response {
        @Schema(description = "회원 고유 식별 ID")
        private Long memberId;
        @Schema(description = "이름")
        private String name;
        @Schema(description = "생년월일")
        private LocalDate birth;
        @Schema(description = "나이")
        private Integer memberAge;
        @Schema(description = "성별")
        private Gender gender;
        @Schema(description = "이메일")
        private String email;
        @Schema(description = "프로필 이미지 URL")
        private String profileImageUrl;
        @Schema(description = "프로필 공개 상태")
        private MemberPrivacyType visibility;
        @Schema(description = "프로필 입력 완료 여부")
        private boolean profileCompleted;
        @Schema(description = "라이프스타일 목록")
        private List<Lifestyle> lifestyles;
        @Schema(description = "타입/유형")
        private RoomProfileType type;
        @Schema(description = "최소 보증금")
        private Integer minDeposit;
        @Schema(description = "최대 보증금")
        private Integer maxDeposit;
        @Schema(description = "최소 월세")
        @JsonProperty("minMonthlyRent")
        private Integer minMounthRent;
        @Schema(description = "최대 월세")
        @JsonProperty("maxMonthlyRent")
        private Integer maxMounthRent;
        @Schema(description = "입주 가능일")
        private LocalDateTime comeEnableAt;
        @Schema(description = "지역 목록")
        private List<Region> region;
        @Schema(description = "방 프로필 목록")
        private List<RoomProfile> roomProfile;
        @Schema(description = "보증금")
        private Integer deposit;
        @Schema(description = "월세")
        @JsonProperty("monthlyRent")
        private Integer mounthRent;

        @Data
        public static class Lifestyle {
            @Schema(description = "고유 식별 ID")
            private Long id;
            @Schema(description = "생활패턴 고유 식별 ID")
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

        @Data
        public static class Region {
            @Schema(description = "고유 식별 ID")
            private Long regionId;
            @Schema(description = "지역명")
            private String region;
        }

        @Data
        public static class RoomProfile {
            @Schema(description = "고유 식별 ID")
            private Long roomProfileId;
            @Schema(description = "방 프로필 이름")
            private String roomProfileName;
        }
    }
}
