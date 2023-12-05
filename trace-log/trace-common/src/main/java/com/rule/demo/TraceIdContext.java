package com.rule.demo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class TraceIdContext {
    public static final String TRACE_ID_KEY = "requestId";

    public static String getTraceId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        return traceId == null ? "" : traceId;
    }

    public static void setTraceId(String traceId) {
        if (StringUtils.isNotEmpty(traceId)) {
            MDC.put(TRACE_ID_KEY, traceId);
        }
    }

    public static void removeTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }

    public static void clearTraceId() {
        MDC.clear();
    }

}
