package com.rule.controller;

import com.rule.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApi {

    @Autowired
    private UserServiceImpl service;

    @GetMapping("/test")
    public Object test(@RequestParam(value = "type", defaultValue = "1") String type) {

        Object result = null;
        switch (type) {
            case "1": result = service.selectAllByMaster(); break;
            case "2": result = service.selectAllBySlave1(); break;
            case "3": result = service.selectAllBySlave2(); break;
            default:break;
        }
        return result;
    }

}
