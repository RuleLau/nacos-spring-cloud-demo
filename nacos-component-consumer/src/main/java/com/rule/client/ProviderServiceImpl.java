package com.rule.client;

import org.springframework.stereotype.Component;

@Component
public class ProviderServiceImpl implements HystrixClient{

    @Override
    public String hystrix(Integer id) {
        return "调用远程服务错误了";
    }
}
