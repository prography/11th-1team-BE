package org.example.knockin.controller;

import lombok.RequiredArgsConstructor;
import org.example.knockin.auth.util.TokenProvider;
import org.example.knockin.dto.PrincipalDetails;
import org.example.knockin.entity.member.Member;
import org.example.knockin.exception.BusinessException;
import org.example.knockin.exception.MemberErrorCode;
import org.example.knockin.repository.member.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/auth")
@RequiredArgsConstructor
public class TestAuthController {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @GetMapping("/token")
    public ResponseEntity<?> getTestToken() {
        Member member = memberRepository.findById(4L).orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        PrincipalDetails principal = new PrincipalDetails(member);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        String accessToken = tokenProvider.generateAccessToken(auth);
        return ResponseEntity.ok(accessToken);
    }
}
