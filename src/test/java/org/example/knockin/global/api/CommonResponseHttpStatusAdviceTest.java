package org.example.knockin.global.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

class CommonResponseHttpStatusAdviceTest {

    private final CommonResponseHttpStatusAdvice advice = new CommonResponseHttpStatusAdvice();

    @Test
    @DisplayName("응답 본문의 상태 코드를 실제 HTTP 상태 코드에도 반영한다")
    void appliesStatusToHttpResponse() {
        CommonResponse<String> body = CommonResponse.status(HttpStatus.CONFLICT).body("duplicate");
        MethodParameter returnType = mock(MethodParameter.class);
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        ServerHttpResponse response = mock(ServerHttpResponse.class);

        CommonResponse<?> result = advice.beforeBodyWrite(
                body,
                returnType,
                MediaType.APPLICATION_JSON,
                StringHttpMessageConverter.class,
                request,
                response
        );

        assertThat(result).isSameAs(body);
        verify(response).setStatusCode(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("CommonResponse 반환 타입에만 advice를 적용한다")
    void supportsCommonResponseOnly() throws NoSuchMethodException {
        Method endpoint = TestEndpoint.class.getDeclaredMethod("response");
        MethodParameter returnType = new MethodParameter(endpoint, -1);

        assertThat(advice.supports(returnType, StringHttpMessageConverter.class)).isTrue();
    }

    private static class TestEndpoint {
        @SuppressWarnings("unused")
        CommonResponse<String> response() {
            return CommonResponse.status(HttpStatus.OK).body("ok");
        }
    }
}
