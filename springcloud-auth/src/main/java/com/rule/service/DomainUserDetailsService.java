package com.rule.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@Slf4j
public class DomainUserDetailsService implements UserDetailsService {
//    @Autowired
//    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 数据库中查询用户信息
//        User user =  userMapper.findByUserName(username);
        List<? extends GrantedAuthority> authorities = new ArrayList();
        return  new org.springframework.security.core.userdetails.User("root","123456",authorities);
    }
}

