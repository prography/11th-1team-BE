package org.example.knockin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements InitializingBean  {
    private final RequestMappingHandlerAdapter adapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> defaultHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();

        List<HttpMessageConverter<?>> converters = adapter.getMessageConverters();

        CustomRequestResponseBodyMethodProcessor customProcessor = new CustomRequestResponseBodyMethodProcessor(converters);

        newHandlers.add(customProcessor);
        if (defaultHandlers != null) {
            newHandlers.addAll(defaultHandlers);
        }

        adapter.setReturnValueHandlers(newHandlers);
    }
}
