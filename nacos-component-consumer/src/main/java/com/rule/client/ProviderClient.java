package com.rule.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@Component
@FeignClient(value = "nacos-component-provider")
public interface ProviderClient {

    @GetMapping("test/{id}")
    String test(@PathVariable("id") Integer id);
}
