package com.rule.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.rule.entity.User;
import com.rule.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @DS("master")
    public List<User> selectAllByMaster() {
        return userMapper.selectList(null);
    }

    @Override
    @DS("one")
    public List<User> selectAllBySlave1() {
        return userMapper.selectList(null);
    }

    @DS("two")
    public List<User> selectAllBySlave2() {
        return userMapper.selectList(null);
    }
}
