package com.rule.controller;

import com.rule.entity.MessageInfo;
import com.rule.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class SendController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageService messageService;

    @GetMapping("/send/trek")
    public boolean sendTrek() {
        // <2>创建 Message
        MessageInfo message = new MessageInfo();
        message.setId(new Random().nextInt());
        return messageService.sendTrek(message);
    }

    @GetMapping("/send/gang")
    public boolean send() {
        // <2>创建 Message
        MessageInfo message = new MessageInfo();
        message.setId(new Random().nextInt());
        return messageService.sendGang(message);
    }

    @GetMapping("/sendDelay/{channel}")
    public boolean sendDelay(@PathVariable("channel") String channel) {
        // 创建 Message
        MessageInfo message = new MessageInfo();
        message.setId(new Random().nextInt());
        message.setDelay("3");
        // 发送消息
        boolean sendResult = messageService.sendTrek(message);
        logger.info("[sendDelay][发送消息完成, 结果 = {}]", sendResult);
        return sendResult;
    }

    @GetMapping("/sendTag/{channel}")
    public boolean sendTag(@PathVariable("channel") String channel) {
        for (String tag : new String[]{"trek", "specialized", "look"}) {
            // 创建 Message
            MessageInfo message = new MessageInfo();
            message.setId(new Random().nextInt());
            // 发送消息
            messageService.sendTrek(message);
        }
        return true;
    }

}



