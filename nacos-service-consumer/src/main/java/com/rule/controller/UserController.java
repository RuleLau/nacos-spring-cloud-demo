package com.rule.controller;

import com.rule.mapper.UserMapper;
import com.rule.pojo.UserInfo;
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
    public void updateUserInfo(@RequestParam("id") String id,
                               @RequestParam("username") String username) {

        // 修改用户名
        UserInfo userInfo = UserInfo.builder().username(username).password("123456").authorities("123").build();
        userMapper.insert(userInfo);
//        userMapper.updateById(updateUserInfo);
    }
}
