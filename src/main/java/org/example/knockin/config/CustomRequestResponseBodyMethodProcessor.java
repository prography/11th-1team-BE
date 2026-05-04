package org.example.knockin.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.knockin.global.api.CommonResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotatedMethod;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CustomRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {
    public CustomRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        CommonResponse<?> httpEntity = null;
        if (returnValue instanceof CommonResponse response) {
            httpEntity = new CommonResponse<>(response.getBody(), response.getStatus(), response.getError());
        }

        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
        HttpHeaders outputHeaders = outputMessage.getHeaders();

        if (httpEntity instanceof CommonResponse<?> responseEntity) {
            HttpStatusCode returnStatus = responseEntity.getStatus();
            outputMessage.getServletResponse().setStatus(returnStatus.value());
            if (returnStatus.value() == HttpStatus.OK.value()) {
                HttpMethod method = inputMessage.getMethod();
                if ((HttpMethod.GET.equals(method) || HttpMethod.HEAD.equals(method)) && isResourceNotModified(inputMessage, outputMessage)) {
                    outputMessage.flush();
                    return;
                }
            } else if (returnStatus.is3xxRedirection()) {
                String location = outputHeaders.getFirst(HttpHeaders.LOCATION);
                if (location != null) {
                    saveFlashAttributes(mavContainer, webRequest, location);
                }
            }
        }
    }

    private boolean isResourceNotModified(ServletServerHttpRequest request, ServletServerHttpResponse response) {
        ServletWebRequest servletWebRequest =
                new ServletWebRequest(request.getServletRequest(), response.getServletResponse());
        HttpHeaders responseHeaders = response.getHeaders();
        String etag = responseHeaders.getETag();
        long lastModifiedTimestamp = responseHeaders.getLastModified();
        if (request.getMethod() == HttpMethod.GET || request.getMethod() == HttpMethod.HEAD) {
            responseHeaders.remove(HttpHeaders.ETAG);
            responseHeaders.remove(HttpHeaders.LAST_MODIFIED);
        }

        return servletWebRequest.checkNotModified(etag, lastModifiedTimestamp);
    }

    private void saveFlashAttributes(ModelAndViewContainer mav, NativeWebRequest request, String location) {
        mav.setRedirectModelScenario(true);
        ModelMap model = mav.getModel();
        if (model instanceof RedirectAttributes redirectAttributes) {
            Map<String, ?> flashAttributes = redirectAttributes.getFlashAttributes();
            if (!CollectionUtils.isEmpty(flashAttributes)) {
                HttpServletRequest req = request.getNativeRequest(HttpServletRequest.class);
                HttpServletResponse res = request.getNativeResponse(HttpServletResponse.class);
                if (req != null) {
                    RequestContextUtils.getOutputFlashMap(req).putAll(flashAttributes);
                    if (res != null) {
                        RequestContextUtils.saveOutputFlashMap(location, req, res);
                    }
                }
            }
        }
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return CommonResponse.class.isAssignableFrom(returnType.getParameterType());
    }
}
