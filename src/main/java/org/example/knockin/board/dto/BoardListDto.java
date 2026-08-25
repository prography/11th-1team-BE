package org.example.knockin.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import lombok.NoArgsConstructor;
import org.example.knockin.authentication.entity.AuthenticationType;
import org.example.knockin.board.entity.RoommateBoardBadgeType;
import org.example.knockin.member.entity.Gender;

@Data
public class BoardListDto {
    @Data
    public static class Request {
        @Schema(description = "지역 ids")
        private List<Long> regionIds;

        @Schema(description = "방 형태 ids")
        private List<Long> roomTypeIds;

        @Schema(description = "성별")
        private Gender gender;

        @Schema(description = "최소 보증금")
        private Integer minDeposit;

        @Schema(description = "최대 보증금")
        private Integer maxDeposit;

        @Schema(description = "최소 월세")
        private Integer minMounthRent;

        @Schema(description = "최대 월세")
        private Integer maxMounthRent;

        @Schema(description = "검색 키워드 (제목/위치/룸형태)")
        private String keyword;

        @Schema(description = "관심 게시글만 조회")
        private Boolean likedOnly;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "게시물 ID")
        private Long id;

        @Schema(description = "대표 이미지 URL")
        private String imageUrl;

        @Schema(description = "게시물 제목")
        private String title;

        @Schema(description = "보증금")
        private Integer deposit;

        @Schema(description = "월세")
        private Integer monthlyRent;

        @Schema(description = "관리비")
        private Integer managementCost;

        @Schema(description = "룸 형태")
        private List<String> roomTypes;

        @Schema(description = "입주가능시기")
        private LocalDateTime comeableDate;

        @Schema(description = "위치")
        private String regionFullName;

        @Schema(description = "작성자 고유 식별 ID")
        private Long memberId;

        @Schema(description = "작성자 이름")
        private String memberName;

        @Schema(description = "작성자 프로필 사진 URL")
        private String memberProfileImageUrl;

        @Schema(description = "작성자 나이")
        private Integer memberAge;

        @Schema(description = "작성자 성별")
        private Gender gender;

        @Schema(description = "신원 인증")
        private List<AuthenticationType> authentications;

        @Schema(description = "조회수")
        private Long hits;

        @Schema(description = "게시글 태그 뱃지 목록")
        private List<RoommateBoardBadgeType> badges;

        @Schema(description = "관심 여부")
        private boolean interested;

        @Schema(description = "생성일자")
        private LocalDateTime createdAt;
    }
}
