package com.rule.controller;

import cn.hutool.core.util.IdUtil;
import com.rule.entities.User;
import com.rule.entities.UserDTO;
import com.rule.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @auther zzyy
 * @create 2021-05-01 15:02
 */
@Api(description = "用户User接口")
@RestController
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @ApiOperation("数据库新增5条记录")
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public void addUser() {
        for (int i = 1; i <= 5; i++) {
            User user = new User();

            user.setUsername("zzyy" + i);
            user.setPassword(IdUtil.simpleUUID().substring(0, 6));
            user.setSex((byte) new Random().nextInt(2));

            userService.addUser(user);
        }
    }

    @ApiOperation("删除1条记录")
    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.POST)
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @ApiOperation("修改1条记录")
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public void updateUser(@RequestBody UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userService.updateUser(user);
    }

    @ApiOperation("查询1条记录")
    @RequestMapping(value = "/user/find/{id}", method = RequestMethod.GET)
    public User findUserById(@PathVariable Integer id) {
        return userService.findUserById2(id);
    }

    @GetMapping("/redisson/{key}")
    public Object redisson(@PathVariable String key) {
        return userService.redisson(key);
    }

    @GetMapping("/redisson/testLock/{key}")
    public void  testTryLock(HttpServletRequest request,  @PathVariable String key) {
        userService.testLock(request, key);
    }

}
