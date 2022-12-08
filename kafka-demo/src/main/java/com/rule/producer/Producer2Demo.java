package com.rule.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;

public class Producer2Demo {


    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
//        props.put("transactional.id", "my-transactional-id");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 20; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("quickstart-events-1", "this is my first client message" + new Date() + ", count=" + i);
            producer.send(record).get();
        }

    }

//    public static void main(String[] args) {
//        System.out.println("kafka-group-01".hashCode() % 30);
//    }
}
