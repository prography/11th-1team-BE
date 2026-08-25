package org.example.knockin.global.auth.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;
import org.example.knockin.global.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import org.example.knockin.global.auth.util.CookieUtils;
import org.example.knockin.member.entity.Member;
import org.example.knockin.global.api.CommonResponse;
import org.example.knockin.meta.dto.AuthResponse;
import org.example.knockin.dto.PrincipalDetails;
import org.example.knockin.global.exception.AuthErrorCode;
import org.example.knockin.global.auth.util.TokenConstants;
import org.example.knockin.global.auth.util.TokenProvider;
import org.example.knockin.global.KnockInProps;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.member.service.impl.MemberServiceImpl;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;


@NullMarked
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KnockInProps knockInProps;
    private final MemberServiceImpl memberService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String accessToken = tokenProvider.generateAccessToken(authentication);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if(ObjectUtils.isEmpty(principalDetails)) throw new BusinessException(AuthErrorCode.ILLEGAL_LOGIN_ACCESS);
        Member member = principalDetails.getMember();
        AuthResponse authResponse = memberService.findMemberForLogin(member, accessToken);
        CommonResponse<AuthResponse> commonResponse = formatCommonResponse(authResponse);

        if (request.getAttribute("isSdkLogin") != null) {
            if (authResponse.getDeleteInfo().isDelete()) {
                authResponse.setAccessToken(null);
                response.setStatus(AuthErrorCode.MEMBER_IS_DELETE.getHttpStatus().value());
            }
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(commonResponse));
        } else {
            String targetUrl = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue).orElse(knockInProps.getClientSuccessUrl());
            if(targetUrl.startsWith(HttpCookieOAuth2AuthorizationRequestRepository.INAPP_REDIRECT_URI_PARAM)) {
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("accessToken", accessToken)
                        .queryParam("basicInfo", authResponse.isBasicInfo())
                        .queryParam("preferenceInfo", authResponse.isPreferenceInfo())
                        .queryParam("name", authentication.getName());

                if (authResponse.getDeleteInfo() != null && authResponse.getDeleteInfo().isDelete()) {
                    uriBuilder.queryParam("isDelete", true).queryParam("reason", authResponse.getDeleteInfo().getReason());
                }

                String redirectUrl = uriBuilder.build().encode(StandardCharsets.UTF_8).toUriString();

                httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
                response.sendRedirect(redirectUrl);
            } else {
                boolean secureCookie = knockInProps.getClientSuccessUrl().startsWith("https://");
                ResponseCookie accessTokenCookie = ResponseCookie.from(TokenConstants.ACCESS_TOKEN_COOKIE_NAME, accessToken)
                        .httpOnly(true)
                        .secure(secureCookie)
                        .sameSite(secureCookie ? "None" : "Lax")
                        .path("/")
                        .maxAge(TokenProvider.ACCESS_TOKEN_EXPIRE_DURATION)
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
                httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
                response.sendRedirect(targetUrl);
            }
        }
    }

    private CommonResponse<AuthResponse> formatCommonResponse(AuthResponse authResponse) {
        if (authResponse.getDeleteInfo().isDelete()) {
            return CommonResponse.status(AuthErrorCode.MEMBER_IS_DELETE.getHttpStatus()).body(authResponse);
        } else {
            return CommonResponse.status(HttpStatus.OK).body(authResponse);
        }
    }
}
