package com.rule.demo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

public class TraceIdHelper {
    public static final String TRACE_ID = "traceId";
    private static final ThreadLocal<String> TRACE_ID_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 清除traceId
     */
    public static void clearTraceId() {
        TRACE_ID_THREAD_LOCAL.remove();
        MDC.remove(TRACE_ID);
    }

    /**
     * 获取traceId
     *
     * @return
     */
    public static String getTraceId() {
        return TRACE_ID_THREAD_LOCAL.get();
    }

    /**
     * 设置traceId，为空时初始化一个
     *
     * @param traceId
     */
    public static void setTraceId(String traceId) {
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        TRACE_ID_THREAD_LOCAL.set(traceId);
        MDC.put(TRACE_ID, traceId);
    }
}
