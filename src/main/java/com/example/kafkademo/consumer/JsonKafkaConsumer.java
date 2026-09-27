package com.example.kafkademo.consumer;

import com.example.kafkademo.model.Customer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaConsumer {

    /**
     * Receives a customer message after Spring Kafka deserializes the JSON payload.
     *
     * @param customer customer object created from the Kafka JSON message
     */
    @KafkaListener(
            topics = "${app.kafka.customer-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(Customer customer) {
        System.out.println("Customer received from Kafka: " + customer);
    }
}
