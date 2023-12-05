package com.rule.demo;

import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
public class TraceIdFilter implements Filter {
    private static final String TRACE_ID = "requestId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String traceId = httpRequest.getHeader(TRACE_ID);
            TraceIdHelper.setTraceId(traceId);
            filterChain.doFilter(request, response);
        } finally {
            // 清除MDC的traceId值，确保当次请求不会影响其他请求
//            TraceIdHelper.clearTraceId();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}