package org.example.knockin.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import org.example.knockin.entity.member.MemberRole;
import org.example.knockin.auth.filter.CustomOAuth2Filter;
import org.example.knockin.auth.filter.TokenAuthenticationFilter;
import org.example.knockin.auth.filter.TokenExceptionFilter;
import org.example.knockin.auth.handler.CustomAccessDeniedHandler;
import org.example.knockin.auth.handler.CustomAuthenticationEntryPoint;
import org.example.knockin.auth.handler.OAuth2FailureHandler;
import org.example.knockin.auth.handler.OAuth2SuccessHandler;
import org.example.knockin.auth.handler.SecurityErrorResponseWriter;
import org.example.knockin.auth.service.CustomOAuth2UserService;
import org.example.knockin.auth.util.TokenProvider;
import org.example.knockin.global.KnockInProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final TokenProvider tokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final SecurityErrorResponseWriter securityErrorResponseWriter;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final org.example.knockin.auth.util.AppleOAuth2AccessTokenResponseClient appleOAuth2AccessTokenResponseClient;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .logout(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(customAccessDeniedHandler))
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(auth -> auth.authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                        .tokenEndpoint(token -> token.accessTokenResponseClient(appleOAuth2AccessTokenResponseClient))
                        .userInfoEndpoint(c -> c.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new TokenExceptionFilter(securityErrorResponseWriter), TokenAuthenticationFilter.class)
                .addFilterBefore(new CustomOAuth2Filter(clientRegistrationRepository, customOAuth2UserService, oAuth2SuccessHandler, oAuth2FailureHandler), OAuth2AuthorizationRequestRedirectFilter.class)
                .authorizeHttpRequests(request -> request.requestMatchers(
                        "/h2-console/**",
                        "/auth/success",
                        "/error",
                        "/login/**",
                        "/oauth2/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/ws-chat/**",
                        "/auth/verify/webhook"
                ).permitAll()
                .requestMatchers(HttpMethod.GET,
                                "/terms",
                                "/terms/*",
                                "/search/popular",
                                "/meta/lifestyle-patterns",
                                "/meta/room-types",
                                "/meta/regions",
                                "/meta/room-add-options",
                                "/roommate/boards",
                                "/roommate/matches",
                                "/meta/faq",
                                "/meta/faqAll",
                                "/meta/faq/*",
                                "/meta/app-version/**",
                                "/meta/auth-email",
                                "/test/auth/token"
                ).permitAll()
                .requestMatchers("/bo/**").hasAuthority(MemberRole.ADMIN.name()).anyRequest().authenticated())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(KnockInProps knockInProps) {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(knockInProps.getCorsUrls());
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
