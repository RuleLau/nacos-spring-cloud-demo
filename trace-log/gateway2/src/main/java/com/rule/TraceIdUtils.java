package com.rule;

import java.util.UUID;

public class TraceIdUtils {
    /**
     * 生成traceId
     *
     * @return TraceId 基于UUID
     */
    public static String getTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}