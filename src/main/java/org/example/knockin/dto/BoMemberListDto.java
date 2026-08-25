package org.example.knockin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.authentication.entity.ApproveType;
import org.example.knockin.authentication.entity.AuthenticationType;
import org.example.knockin.member.entity.MemberRole;
import org.example.knockin.member.entity.MemberState;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoMemberListDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Schema(description = "이름 또는 이메")
        private String searchName;
        @Schema(description = "상태")
        private MemberState searchState;
        @Schema(description = "신원인증")
        private ApproveType searchApproveType;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "회원 목록")
        private List<MemberInfo> memberInfoList;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MemberInfo {
            @Schema(description = "고유 번호")
            private Long id;
            @Schema(description = "이름")
            private String name;
            @Schema(description = "이메일")
            private String email;
            @Schema(description = "가입일")
            private LocalDateTime createdAt;
            @Schema(description = "신원 인증")
            private AuthenticationType authenticationType;
            @Schema(description = "권한")
            private MemberRole role;
            @Schema(description = "회원 상태")
            private MemberState state;
        }
    }
}