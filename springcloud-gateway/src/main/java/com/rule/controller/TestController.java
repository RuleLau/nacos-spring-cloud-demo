package com.rule.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TestController {


    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        // Mono是一个Reactive stream，对外输出一个“fallback”字符串。
        return Mono.just("fallback");
    }

}
