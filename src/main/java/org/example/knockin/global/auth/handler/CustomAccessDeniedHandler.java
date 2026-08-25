package org.example.knockin.global.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.knockin.global.exception.CommonErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final SecurityErrorResponseWriter securityErrorResponseWriter;

    @Override
    public void handle(
            HttpServletRequest request,
                HttpServletResponse response,
                AccessDeniedException accessDeniedException
        ) throws IOException, ServletException {
            securityErrorResponseWriter.write(response, CommonErrorCode.ACCESS_DENIED);
    }
}
