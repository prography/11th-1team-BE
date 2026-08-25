package org.example.knockin.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.knockin.global.exception.AuthErrorCode;
import org.example.knockin.global.exception.AuthException;
import org.example.knockin.global.auth.util.TokenConstants;
import org.example.knockin.global.auth.util.TokenProvider;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@NullMarked
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request);

        if (tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(TokenConstants.AUTHORIZATION_HEADER);
        if (!ObjectUtils.isEmpty(token)) {
            if (!token.startsWith(TokenConstants.BEARER_PREFIX)) {
                throw new AuthException(AuthErrorCode.TOKEN_INVALID);
            }

            String accessToken = token.substring(TokenConstants.BEARER_PREFIX.length());
            if (!StringUtils.hasText(accessToken)) {
                throw new AuthException(AuthErrorCode.TOKEN_INVALID);
            }

            return accessToken;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (TokenConstants.ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                if (!StringUtils.hasText(cookie.getValue())) {
                    throw new AuthException(AuthErrorCode.TOKEN_INVALID);
                }
                return cookie.getValue();
            }
        }

        return null;
    }
}
