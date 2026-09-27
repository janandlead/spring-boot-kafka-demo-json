package com.example.kafkademo.consumer;

import com.example.kafkademo.model.Customer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaConsumer {

    @KafkaListener(
            topics = "${app.kafka.customer-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(Customer customer) {
        System.out.println("Customer received from Kafka: " + customer);
    }
}
