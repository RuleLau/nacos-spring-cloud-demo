package com.rule.controller;

import com.rule.common.config.MyPasswordEncoder;
import com.rule.common.entity.UserInfo;
import com.rule.common.mapper.UserMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private MyPasswordEncoder passwordEncoder;

    @PostMapping
    public void addUser(@RequestBody UserInfo userInfo) {
        String dbPassword = passwordEncoder.encode(userInfo.getPassword());
        userInfo.setPassword(dbPassword);
        // 默认是管理员
//        userInfo.setAuthorities("ROLE_NORMAL,ROLE_MEDIUM");
        userMapper.insert(userInfo);
    }

    @PostMapping("/getUserInfo")
    public Object getUserInfo(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @GetMapping("/getUserInfoII")
    public Object getUserInfoII(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String jwtToken = authorization.substring("Bearer ".length());
        return Jwts.parser().setSigningKey("SigningKey".getBytes(StandardCharsets.UTF_8))
                .parse(jwtToken).getBody();
    }
}
