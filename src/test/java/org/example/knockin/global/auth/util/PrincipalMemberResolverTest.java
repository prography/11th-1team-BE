package org.example.knockin.global.auth.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.example.knockin.verification.entity.LoginProviderType;
import org.example.knockin.member.entity.Member;
import org.example.knockin.member.entity.MemberRole;
import org.example.knockin.global.auth.dto.PrincipalDetails;
import org.example.knockin.global.exception.AuthErrorCode;
import org.example.knockin.global.exception.AuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@DisplayName("인증 주체 회원 식별자 변환기")
class PrincipalMemberResolverTest {

    private final PrincipalMemberResolver resolver = new PrincipalMemberResolver();

    @Test
    @DisplayName("PrincipalDetails 인증 주체에서 회원 식별자를 추출한다")
    void resolveMemberIdReturnsMemberIdFromPrincipalDetails() {
        // Given
        Authentication authentication = authentication(1L);

        // When
        Long memberId = resolver.resolveMemberId(authentication);

        // Then
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("PrincipalDetails 인증 주체가 아니면 인증 실패 예외를 던진다")
    void resolveMemberIdThrowsAuthExceptionWhenPrincipalIsInvalid() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("member", null);

        // When & Then
        assertThatThrownBy(() -> resolver.resolveMemberId(authentication))
                .isInstanceOfSatisfying(AuthException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(AuthErrorCode.AUTHENTICATION_FAILED));
    }

    private Authentication authentication(Long memberId) {
        Member member = Member.builder()
                .id(memberId)
                .providerType(LoginProviderType.KAKAO)
                .providerId("provider-id")
                .role(MemberRole.USER)
                .build();
        PrincipalDetails details = new PrincipalDetails(member);
        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }
}
