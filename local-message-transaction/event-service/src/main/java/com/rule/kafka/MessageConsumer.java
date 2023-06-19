package com.rule.kafka;

import com.rule.entity.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
//@Component
public class MessageConsumer {

    @KafkaListener(topics = "order", groupId = "event-service")
    public void receive(EventMessage eventMessage) {
        log.info("接收到消息，{}", eventMessage);
    }
}
