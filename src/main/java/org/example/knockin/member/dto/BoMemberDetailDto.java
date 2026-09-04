package org.example.knockin.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.knockin.verification.entity.AuthenticationType;
import org.example.knockin.member.entity.Gender;
import org.example.knockin.member.entity.MemberRole;
import org.example.knockin.member.entity.MemberState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoMemberDetailDto {
    @Data
    public static class Request {
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "고유 번호")
        private Long id;
        @Schema(description = "이름")
        private String name;
        @Schema(description = "이메일")
        private String email;
        @Schema(description = "가입일")
        private LocalDateTime createdAt;
        @Schema(description = "신원 인증")
        private List<AuthenticationInfo> authenticationInfoList;
        @Schema(description = "권한")
        private MemberRole role;
        @Schema(description = "회원 상태")
        private MemberState state;
        @Schema(description = "성별")
        private Gender gender;
        @Schema(description = "생년월일")
        private LocalDate birth;
        @Schema(description = "신고횟수")
        private Integer reportCount;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AuthenticationInfo {
            @Schema(description = "인증 유형")
            private AuthenticationType authenticationType;
            @Schema(description = "인증 이메일")
            private String authenticationEmail;
        }
    }
}