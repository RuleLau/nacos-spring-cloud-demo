package com.rule.cron;

import com.rule.config.MessageSender;
import com.rule.dao.MessageRepository;
import com.rule.entity.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MessageCron {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageSender messageSender;

    @Scheduled(initialDelay = 1000L, fixedRate = 60000L)
    public void scanEventMessage() {

        List<EventMessage> eventMessages = messageRepository.queryPrepareSendMessage();

        List<EventMessage> deleteMessages = new ArrayList<>(eventMessages.size());
        // send mq
        try {
            for (EventMessage eventMessage : eventMessages) {
                //send mq
                messageSender.send(eventMessage);
                deleteMessages.add(eventMessage);
            }
        } finally {
            // delete
            log.info("delete message list");
//            messageRepository.deleteAll(deleteMessages);
        }
    }

}
