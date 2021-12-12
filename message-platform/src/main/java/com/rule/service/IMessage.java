package com.rule.service;

import com.rule.entity.MessageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public interface IMessage {

    boolean sendTrek(MessageInfo message);

    boolean sendGang(MessageInfo message);

    default Message<MessageInfo> messageBuilder(MessageInfo message) {
        MessageBuilder<MessageInfo> messageInfoMessageBuilder = MessageBuilder.withPayload(message);
        if (StringUtils.isNotBlank(message.getTag())) {
            messageInfoMessageBuilder.setHeader(MessageConst.PROPERTY_TAGS, message.getTag());
        }

        if (StringUtils.isNotBlank(message.getDelay())) {
            messageInfoMessageBuilder.setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, message.getDelay());
        }

        return messageInfoMessageBuilder.build();
    }
}
