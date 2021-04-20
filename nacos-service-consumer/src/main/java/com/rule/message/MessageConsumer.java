package com.rule.message;

import com.alibaba.fastjson.JSON;
import com.rule.client.Sink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @StreamListener(Sink.ERBADAGANG_INPUT)
    public void onMessage(@Payload MessageInfo message) {
        logger.info("[onMessage--gang][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), JSON.toJSONString(message));
    }

    @StreamListener(Sink.TREK_INPUT)
    public void onTrekMessage(@Payload MessageInfo message) {
        logger.info("[onMessage--trek][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), JSON.toJSONString(message));
    }
}
