package com.rule.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * consumer client
 */
@FeignClient("consumer-service")
public interface ConsumerClient {

    @GetMapping("/echo")
    String echo();

}
