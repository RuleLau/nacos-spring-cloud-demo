package com.rule.demo;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(TraceIdContext.TRACE_ID_KEY, TraceIdContext.getTraceId());
    }

}
