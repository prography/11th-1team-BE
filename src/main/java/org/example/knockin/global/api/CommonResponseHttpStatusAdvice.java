package org.example.knockin.global.api;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class CommonResponseHttpStatusAdvice implements ResponseBodyAdvice<CommonResponse<?>> {

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return CommonResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public CommonResponse<?> beforeBodyWrite(
            CommonResponse<?> body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        // 본문의 status만 바꾸면 앱은 계속 성공으로 본다. 실제 HTTP 상태도 같이 맞춘다.
        if (body != null) {
            response.setStatusCode(body.getStatus());
        }
        return body;
    }
}
