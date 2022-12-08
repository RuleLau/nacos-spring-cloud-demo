package com.rule.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.ByteBuffer;
import java.util.Properties;

public class ProducerDemo3 {


    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
//        props.put("transactional.id", "my-transactional-id");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteBufferSerializer");
        Producer<String, ByteBuffer> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 20; i++) {
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            allocate.put("XXX".getBytes());
            ProducerRecord<String, ByteBuffer> record = new ProducerRecord<>("quickstart-events-3", allocate);
            producer.send(record).get();
        }

    }

//    public static void main(String[] args) {
//        System.out.println("kafka-group-01".hashCode() % 30);
//    }
}
