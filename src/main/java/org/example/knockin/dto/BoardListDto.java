package org.example.knockin.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.example.knockin.entity.auth.AuthenticationType;
import org.example.knockin.entity.board.RoommateBoardBadgeType;
import org.example.knockin.entity.member.Gender;

@Data
public class BoardListDto {
    @Data
    public static class Request {
        @Schema(description = "지역 ids")
        List<Long> regionIds;

        @Schema(description = "방 형태 ids")
        List<Long> roomTypeIds;

        @Schema(description = "성별")
        Gender gender;

        @Schema(description = "최소 보증금")
        Integer minDeposit;

        @Schema(description = "최대 보증금")
        Integer maxDeposit;

        @Schema(description = "최소 월세")
        @JsonProperty("minMonthlyRent")
        @JsonAlias("minMounthRent")
        Integer minMounthRent;

        @Schema(description = "최대 월세")
        @JsonProperty("maxMonthlyRent")
        @JsonAlias("maxMounthRent")
        Integer maxMounthRent;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @Schema(description = "게시물 ID")
        Long id;

        @Schema(description = "대표 이미지 URL")
        String imageUrl;

        @Schema(description = "게시물 제목")
        String title;

        @Schema(description = "보증금")
        Integer deposit;

        @Schema(description = "월세")
        Integer monthlyRent;

        @Schema(description = "관리비")
        Integer managementCost;

        @Schema(description = "룸 형태")
        List<String> roomTypes;

        @Schema(description = "입주가능시기")
        LocalDateTime comeableDate;

        @Schema(description = "위치")
        String regionFullName;

        @Schema(description = "작성자 이름")
        String memberName;

        @Schema(description = "신원 인증")
        List<AuthenticationType> authentications;

        @Schema(description = "조회수")
        Long hits;

        @Schema(description = "인증배지")
        List<RoommateBoardBadgeType> badges;
    }
}
