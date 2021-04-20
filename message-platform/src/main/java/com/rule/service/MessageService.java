package com.rule.service;

import com.rule.pojo.MessageInfo;
import com.rule.source.MessagePlatformSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageService implements IMessage {

    @Autowired
    private MessagePlatformSource messageSource;


    @Override
    public boolean sendTrek(MessageInfo message) {
        // <1>创建 Spring Message 对象
        Message<MessageInfo> springMessage = messageBuilder(message);
        // <2>发送消息
        return messageSource.trekOutput().send(springMessage);
    }

    @Override
    public boolean sendGang(MessageInfo message) {
        // <1>创建 Spring Message 对象
        Message<MessageInfo> springMessage = messageBuilder(message);
        // <2>发送消息
        return messageSource.erbadagangOutput().send(springMessage);
    }
}
