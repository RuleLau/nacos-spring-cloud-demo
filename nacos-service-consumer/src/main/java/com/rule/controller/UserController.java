package com.rule.controller;

import com.rule.mapper.UserMapper;
import com.rule.entity.UserInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserMapper userMapper;

    @PostMapping
    @Transactional
    public void updateUserInfo(@RequestParam("id") String id,
                               @RequestParam("username") String username) {

        // 增加一条用户信息
        UserInfo userInfo = UserInfo.builder().username(username).password("123456").authorities("123").build();
        userMapper.insert(userInfo);
    }
}
