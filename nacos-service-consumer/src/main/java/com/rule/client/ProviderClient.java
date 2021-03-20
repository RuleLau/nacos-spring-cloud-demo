package com.rule.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("nacos-service-provider")
public interface ProviderClient {

    @GetMapping("/echo/{string}")
    String echo(@PathVariable(value = "string") String name);
}
