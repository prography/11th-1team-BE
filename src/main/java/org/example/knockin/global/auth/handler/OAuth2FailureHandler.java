package org.example.knockin.global.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.knockin.global.exception.AuthErrorCode;
import org.example.knockin.global.KnockInProps;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    private final KnockInProps knockInProps;
    private final SecurityErrorResponseWriter securityErrorResponseWriter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (request.getAttribute("isSdkLogin") != null) {
            securityErrorResponseWriter.write(response, AuthErrorCode.OAUTH_LOGIN_FAILED);
        } else {
            response.sendRedirect(knockInProps.getClientErrorUrl() + AuthErrorCode.OAUTH_LOGIN_FAILED.name());
        }
    }
}
