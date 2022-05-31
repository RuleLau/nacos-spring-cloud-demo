package com.rule.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HystrixApi {


    @GetMapping("/with/hystrix")
    @HystrixCommand(fallbackMethod = "fallback")
    public String withHystrix() {
        int i = 1;
        int b = i / 0;
        return "success";
    }

    public String fallback() {
        return "fallback";
    }

    @GetMapping("/without/hystrix")
    public String withoutHystrix() {

        int i = 1;
        int b = i / 0;
        return "success";
    }

}
