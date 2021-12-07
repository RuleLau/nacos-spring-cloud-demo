package com.rule.base;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

@Slf4j
public class DefaultProducer {

    public static void main(String[] args) throws Exception {

        start("instance1");

        start("instance2");
    }

    public static void start(String instanceName) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("rocketmq-demo");
        producer.setNamesrvAddr("192.168.25.21:9877");
        producer.setVipChannelEnabled(false);
        producer.setRetryTimesWhenSendAsyncFailed(10);
        producer.setInstanceName(instanceName);
        producer.start();
        log.info("rocketmq producer server开启成功---------------------------------.");

        for (int i = 0; i < 5; i++) {
            Message message = new Message("rocketmq-cluster", "Tag1", "12345", "rocketmq测试成功".getBytes());
            // 这里用到了这个mq的异步处理，类似ajax，可以得到发送到mq的情况，并做相应的处理
            //不过要注意的是这个是异步的
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info(System.currentTimeMillis() + " -- 传输成功");
                    log.info(JSON.toJSONString(sendResult));
                }
                @Override
                public void onException(Throwable e) {
                    log.error("传输失败", e);
                }
            });
        }

    }
}
