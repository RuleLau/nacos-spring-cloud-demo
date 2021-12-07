package com.rule.service;

import com.rule.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestLogService {

    @Autowired
    TestLogService testLogService;

    @Log
    public void sayHi(String msg) {
        System.out.println("sayHi:" + msg);
        sing("Up and Down");
    }


    @Log
    public void sayHi2(String msg) {
        System.out.println("sayHi:" + msg);
//        TestLogService testLogService = (TestLogService) AopContext.currentProxy();
        testLogService.sing("Up and Down");
    }

    @Log
    public void sing(String music) {
        System.out.println("sing : " + music);
    }
}
