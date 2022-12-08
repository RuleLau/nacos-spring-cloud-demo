package com.rule.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rule.demo.LrHystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HystrixApi {

    @Autowired
    private RestTemplate restTemplate;

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

    @GetMapping("/hello")
    public String hello(@RequestParam("id") Integer id) {
        return "success: " + id;
    }

    @GetMapping("/lr/hystrix")
    @LrHystrixCommand(fallback = "customFallback", timeout = 3000)
    public String lrHystrix() {

        Map<String, Integer> map = new HashMap<>();
        map.put("id", 666);
        return restTemplate.getForObject("http://localhost:8080/hello?id={id}", String.class, map);
    }

    public String customFallback() {
        return "custom 请求被降级";
    }

}
