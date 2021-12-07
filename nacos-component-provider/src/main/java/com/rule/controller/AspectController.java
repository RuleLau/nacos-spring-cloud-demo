package com.rule.controller;

import com.rule.service.TestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AspectController {

    @Autowired
    TestLogService testLogService;

    @GetMapping("/aspect/test")
    public String testAspect() {

        testLogService.sayHi("jack");
        return "ok";
    }

    @GetMapping("/aspect/test2")
    public String testAspect2() {

        testLogService.sayHi2("jack");
        return "ok";
    }
}
