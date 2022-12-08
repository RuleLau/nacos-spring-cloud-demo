package com.rule.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

public class ConsumerDemo4 {


    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "consumer4");
//        props.put("transactional.id", "my-transactional-id");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("quickstart-events-3"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
            if (records.isEmpty()) {
                Set<TopicPartition> assignment = consumer.assignment();
                for (TopicPartition topicPartition : assignment) {
                    consumer.seek(topicPartition, 0);;
                }
            }
            for (ConsumerRecord<String, String> record : records) {
                System.out.print("ConsumerDemo1 处理消息，分区为：" + record.partition() + ", 偏移量为：" + record.offset());
                System.out.println(", 消息为：" + record.value());
            }
        }
    }
}
