package com.rule.kafka;

import com.alibaba.fastjson.JSON;
import com.rule.entity.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class MessageConsumer {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @KafkaListener(topics = "order", groupId = "event-service")
    public void receive1(ConsumerRecord<String, String> consumerRecord) {
        String value = consumerRecord.value();
        try {
            EventMessage eventMessage = JSON.parseObject(value, EventMessage.class);
            log.info("接收到消息，{}", eventMessage.getPayload());
            // 处理扣减余额操作
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<EventMessage> entity = new HttpEntity<>(eventMessage, headers);

            ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:6002/deduceUserBalance", entity, String.class);
            String body = response.getBody();
            log.info("扣款结果：{}", body);
        } catch (Exception e) {
            log.error("转化消息失败，consumerRecord: {}", consumerRecord);
        }
    }
}
