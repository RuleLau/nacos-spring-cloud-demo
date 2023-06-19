package com.rule.kafka;

import com.alibaba.fastjson.JSON;
import com.rule.entity.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

//@Component
@Slf4j
public class MessageSender {

    // kafka话题
    @Value("${kafka.topic}")
    private String topic;
    // kafka分区
    @Value("${kafka.partNum}")
    private int partNum;
    // Kafka备份数
    @Value("${kafka.repeatNum}")
    private short repeatNum;
    // kafka地址
    @Value("${spring.kafka.bootstrap-servers}")
    private String host;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostConstruct
    public void createTopic() {
        // 创建topic
        Properties props = new Properties();
        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        NewTopic newTopic = new NewTopic(topic, partNum, repeatNum);
        AdminClient adminClient = AdminClient.create(props);
        List<NewTopic> topicList = Collections.singletonList(newTopic);
        adminClient.createTopics(topicList);
        adminClient.close();
    }

    public void send(EventMessage eventMessage) {
        // 发送消息
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, JSON.toJSONString(eventMessage));
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("Produce: The message failed to be sent:" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> stringObjectSendResult) {
                log.info("Produce: The message was sent successfully:");
                log.info("Produce: _+_+_+_+_+_+_+ result: " + stringObjectSendResult.toString());
            }
        });
    }
}
