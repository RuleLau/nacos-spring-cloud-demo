package com.rule.controller;

import com.rule.entity.User;
import com.rule.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApi {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/test")
    public String test() {

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("test" + i);
            user.setPassword(String.valueOf(i % 2));
            user.setAuthorities("admin");
            userMapper.save(user);
        }
        return "success";
    }

}
