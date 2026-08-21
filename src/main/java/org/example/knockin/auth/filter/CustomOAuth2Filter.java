package org.example.knockin.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.knockin.dto.AppleSdkRequest;
import org.example.knockin.dto.OAuth2SdkRequest;
import org.example.knockin.auth.handler.OAuth2FailureHandler;
import org.example.knockin.auth.handler.OAuth2SuccessHandler;
import org.example.knockin.auth.util.OAuth2SdkProvider;
import org.example.knockin.exception.AuthErrorCode;
import org.example.knockin.exception.AuthException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;


@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2Filter extends OncePerRequestFilter {
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final String LOGIN_PATTERN = "/sdk/oauth2/authorization/{registrationId}";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (pathMatcher.match(LOGIN_PATTERN, servletPath) && HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            try {
                Map<String, String> variables = pathMatcher.extractUriTemplateVariables(LOGIN_PATTERN, servletPath);
                String registrationId = variables.get("registrationId");

                Class<? extends OAuth2SdkRequest> dtoClass = OAuth2SdkProvider
                        .findByRegistrationId(registrationId)
                        .getDtoClass();

                OAuth2SdkRequest sdkRequest = objectMapper.readValue(request.getInputStream(), dtoClass);
                String accessToken = sdkRequest.getAccessToken();

                if (accessToken == null || accessToken.isEmpty()) {
                    throw new AuthException(AuthErrorCode.ACCESS_TOKEN_OMISSION);
                }

                ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
                if (clientRegistration == null) {
                    throw new AuthException(AuthErrorCode.NOT_SUPPORT_SOCIAL_LOGIN);
                }

                OAuth2AccessToken oauth2Token = new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        accessToken,
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                );

                Map<String, Object> additionalParameters = new HashMap<>();
                if (sdkRequest instanceof AppleSdkRequest appleReq) {
                    if (appleReq.getName() != null) {
                        additionalParameters.put("name", appleReq.getName());
                    }
                }

                OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, oauth2Token, additionalParameters);
                OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

                OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                        oAuth2User,
                        oAuth2User.getAuthorities(),
                        clientRegistration.getRegistrationId()
                );

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

                request.setAttribute("isSdkLogin", true);
                oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            } catch (Exception e) {
                log.error("로그인 실패: message={}", e.getMessage());
                SecurityContextHolder.clearContext();
                request.setAttribute("isSdkLogin", true);
                AuthenticationException authException = new InternalAuthenticationServiceException(e.getMessage(), e);
                oAuth2FailureHandler.onAuthenticationFailure(request, response, authException);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
