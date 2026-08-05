package org.example.knockin.config;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.knockin.exception.AuthErrorCode;
import org.example.knockin.exception.AuthException;
import org.example.knockin.auth.util.PrincipalMemberResolver;
import org.example.knockin.auth.util.TokenConstants;
import org.example.knockin.auth.util.TokenProvider;
import org.example.knockin.exception.BusinessException;
import org.example.knockin.service.impl.ChatRoomMemberServiceImpl;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompAuthenticationChannelInterceptor implements ChannelInterceptor {
    public static final String TOKEN_EXPIRES_AT_SESSION_KEY = "TOKEN_EXPIRES_AT";

    private final TokenProvider tokenProvider;
    private final StompDestinationResolver stompDestinationResolver;
    private final PrincipalMemberResolver principalMemberResolver;
    private final ChatRoomMemberServiceImpl chatRoomMemberService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        try {
            switch (accessor.getCommand()) {
                case CONNECT -> handleConnect(accessor);
                case SEND -> handleSend(accessor);
                case SUBSCRIBE -> handleSubscribe(accessor);
            }
        } catch (AuthException e) {
            log.warn("STOMP 인증 실패 [{}]: {}", e.getErrorCode().name(), e.getErrorCode().getMessage());
            throw e;
        } catch (BusinessException e) {
            log.warn("STOMP 인가 실패 [{}]: {}", e.getErrorCode().name(), e.getErrorCode().getMessage());
            throw e;
        } catch (RuntimeException e) {
            log.warn("그 외 실패 [{}]: {}", e.getMessage());
            throw e;
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        log.info("STOMP 인증 시작");
        String accessToken = resolveAccessToken(accessor);
        Authentication authentication = authenticate(accessToken);
        Date expiresAt = tokenProvider.getExpiration(accessToken);

        accessor.setUser(authentication);
        getSessionAttributes(accessor).put(TOKEN_EXPIRES_AT_SESSION_KEY, expiresAt);
    }

    private void handleSend(StompHeaderAccessor accessor) {
        authenticateSession(accessor);
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        authenticateSession(accessor);
        authorizeChatSubscription(accessor);
    }

    private Authentication authenticate(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }

        return tokenProvider.getAuthentication(accessToken);
    }

    private void authenticateSession(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (!(principal instanceof Authentication authentication) || !authentication.isAuthenticated()) {
            throw new AuthException(AuthErrorCode.AUTHENTICATION_FAILED);
        }

        Object expiresAt = getSessionAttributes(accessor).get(TOKEN_EXPIRES_AT_SESSION_KEY);
        if (!(expiresAt instanceof Date tokenExpiresAt)) {
            throw new AuthException(AuthErrorCode.AUTHENTICATION_FAILED);
        }

        if (!tokenExpiresAt.after(new Date())) {
            throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
        }
    }

    private void authorizeChatSubscription(StompHeaderAccessor accessor) {
        var chatRoomId = stompDestinationResolver.resolveChatSubscribeRoomId(accessor.getDestination());
        if (chatRoomId.isEmpty()) {
            return;
        }
        Long memberId = principalMemberResolver.resolveMemberId(accessor.getUser());
        chatRoomMemberService.checkCanSubscribe(chatRoomId.get(), memberId);
    }

    private String resolveAccessToken(StompHeaderAccessor accessor) {
        String authorization = accessor.getFirstNativeHeader(TokenConstants.AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authorization) || !authorization.startsWith(TokenConstants.BEARER_PREFIX)) {
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }

        String accessToken = authorization.substring(TokenConstants.BEARER_PREFIX.length());
        if (!StringUtils.hasText(accessToken)) {
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }

        return accessToken;
    }

    private Map<String, Object> getSessionAttributes(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes == null) {
            sessionAttributes = new HashMap<>();
            accessor.setSessionAttributes(sessionAttributes);
        }
        return sessionAttributes;
    }
}
