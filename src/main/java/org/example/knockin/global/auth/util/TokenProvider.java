package org.example.knockin.global.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.knockin.member.entity.Member;
import org.example.knockin.dto.PrincipalDetails;
import org.example.knockin.global.exception.AuthErrorCode;
import org.example.knockin.global.exception.AuthException;
import org.example.knockin.member.service.impl.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TokenProvider {
    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;
    public static final Duration ACCESS_TOKEN_EXPIRE_DURATION = Duration.ofDays(7);
    private static final long ACCESS_TOKEN_EXPIRE_TIME = ACCESS_TOKEN_EXPIRE_DURATION.toMillis();
    private static final String KEY_ROLE = "role";
    private static final String KEY_MEMBER_ID = "memberId";

    private final MemberServiceImpl memberService;

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication);
    }

    private String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TokenProvider.ACCESS_TOKEN_EXPIRE_TIME);
        PrincipalDetails principal = getPrincipalDetails(authentication);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(KEY_ROLE, authorities)
                .claim(KEY_MEMBER_ID, principal.getMember().getId())
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    private PrincipalDetails getPrincipalDetails(Authentication authentication) {
        if (authentication.getPrincipal() instanceof PrincipalDetails principalDetails) {
            return principalDetails;
        }
        throw new AuthException(AuthErrorCode.AUTHENTICATION_FAILED);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        Member member = getMember(claims);
        PrincipalDetails principal = new PrincipalDetails(member);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private Member getMember(Claims claims) {
        Long memberId = claims.get(KEY_MEMBER_ID, Long.class);
        if (memberId == null) {
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }
        return memberService.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.TOKEN_INVALID));
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Arrays.stream(claims.get(KEY_ROLE).toString().split(","))
                .filter(auth -> !auth.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return claims.getExpiration().after(new Date());
    }

    public Date getExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }
    }
}
