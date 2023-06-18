package com.rule.controller;

import com.rule.entity.User;
import com.rule.mapper.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public List<User> findOneCity() {
        List<String> nameList = new ArrayList<>();
        nameList.add("jarry");
        nameList.add("admin");
        nameList.add("zzzz");

        return userDao.findByNameList(nameList);
    }
}
