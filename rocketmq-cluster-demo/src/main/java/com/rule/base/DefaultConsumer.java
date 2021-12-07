package com.rule.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class DefaultConsumer {


    public static void main(String[] args) throws Exception {

        startConsumer(false, "instanceName1");
        startConsumer(false, "instanceName2");
    }
    
    
    public static DefaultMQPushConsumer startConsumer(boolean isVip, String instanceName) throws Exception{

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("rocketmq-demo");
        consumer.setNamesrvAddr("192.168.25.21:9877");
        consumer.subscribe("rocketmq-cluster", "*");
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setInstanceName(instanceName);
        consumer.setVipChannelEnabled(isVip);
//        consumer.setInstanceName(topicName);
        // 开启内部类实现监听
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                log.info(consumer.getInstanceName() + "开始消费，消息容量：" + msgs.size() + ", "
                        + context.getMessageQueue().getBrokerName() + ", messageId: " + msgs.get(0).getMsgId()
                        + ", 内容：" + new String(msgs.get(0).getBody(), StandardCharsets.UTF_8));
                return dealBody(msgs, context);
            }
        });
        consumer.start();
        log.info("rocketmq消费端启动成功----------------------------------- instanceName: " + instanceName);
        return consumer;
    }

    // 处理body的业务
    public static ConsumeConcurrentlyStatus dealBody(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
