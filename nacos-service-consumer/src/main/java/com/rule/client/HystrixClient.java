package com.rule.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "nacos-service-provider",fallback = ProviderServiceImpl.class)
public interface HystrixClient {

    @GetMapping("hystrixI/{id}")
    public String hystrix(@PathVariable("id") Integer id);
}
