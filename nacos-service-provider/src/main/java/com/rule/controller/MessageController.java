package com.rule.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rule.client.UserClient;
import com.rule.mapper.MessageMapper;
import com.rule.pojo.MessageInfo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 生产者服务，测试seata
 */
@RestController
@RequestMapping("/provider")
public class MessageController {

    @Resource
    private MessageMapper messageMapper;

    @Autowired
    private UserClient userClient;

    /**
     * 发送消息修改用户信息
     */
    @GetMapping("/send")
    // @GlobalTransactional  配置全局的事务方案
    @GlobalTransactional
    public void sendMessage() {
        // 添加消息
        MessageInfo messageInfo = MessageInfo.builder().title("update user username")
                .body("{\"id\":\"1\", \"username\":\"jarry\"}")
                .type("send").build();
        messageMapper.insert(messageInfo);
        JSONObject body = JSON.parseObject(messageInfo.getBody());
        // 调用userClient 增加用户信息
        String username = body.getString("username") + System.currentTimeMillis();
        userClient.updateUserInfo(body.getString("id"), username);
        throw new RuntimeException("修改失败");
    }
}
