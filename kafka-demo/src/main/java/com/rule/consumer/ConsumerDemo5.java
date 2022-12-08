package com.rule.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 多线程消费
 */
public class ConsumerDemo5 {


    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "consumer5");
//        props.put("transactional.id", "my-transactional-id");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList("quickstart-events-5"));
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(2));
            if (records.isEmpty()) {
                continue;
            }
            executorService.submit(() -> {
                for (ConsumerRecord<String, String> record : records) {
                    System.out.print("ConsumerDemo1 处理消息，分区为：" + record.partition() + ", 偏移量为：" + record.offset());
                    System.out.println(", 消息为：" + record.value());
                }
            });
        }
    }
}
