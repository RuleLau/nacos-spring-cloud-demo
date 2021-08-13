package com.rule.service;

import com.rule.config.ConsumerConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@Log4j2
public abstract class DefaultConsumerConfigure {

    @Autowired
    private ConsumerConfig consumerConfig;

    // 开启消费者监听服务
    public void listener(String topic, String tag) throws MQClientException {
        log.info("开启" + topic + ":" + tag + "消费者-------------------");
        log.info(consumerConfig.toString());

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerConfig.getGroupName());

        consumer.setNamesrvAddr(consumerConfig.getNamesrvAddr());

        consumer.subscribe(topic, tag);

        // 开启内部类实现监听
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                return dealBody(msgs);
            }
        });

        consumer.start();

        log.info("rocketmq消费端启动成功---------------------------------------");

    }

    // 处理body的业务
    public ConsumeConcurrentlyStatus dealBody(List<MessageExt> msgs) {
        int num = 1;
        log.info("进入");
        for (MessageExt msg : msgs) {
            log.info("第" + num + "次消息");
            String msgStr = new String(msg.getBody(), StandardCharsets.UTF_8);
            log.info(msgStr);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
