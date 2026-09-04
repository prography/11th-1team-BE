package org.example.knockin.global.auth.handler;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.knockin.global.api.CommonResponse;
import org.example.knockin.global.api.ErrorResponse;
import org.example.knockin.global.exception.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SecurityErrorResponseWriter {
    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        
        CommonResponse<Void> commonResponse = CommonResponse.status(errorCode.getHttpStatus()).error(ErrorResponse.of(errorCode));
        objectMapper.writeValue(response.getWriter(), commonResponse);
    }
}
