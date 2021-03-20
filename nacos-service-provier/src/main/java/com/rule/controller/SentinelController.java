package com.rule.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SentinelController {

    @GetMapping("/hello")
    @SentinelResource(value = "hello")
    public String hello(@RequestParam(value = "name") String name) {
        return "hello " + name;
    }
}
