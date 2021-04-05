package com.rule.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

/**
 * 读取配置信息
 */
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${useLocalCache}")
    private boolean useLocalCache;

    @Value("${spring.redis.host}")
    private String host;

    @GetMapping("/get")
    public Object get() {
        return host + useLocalCache;
    }
}