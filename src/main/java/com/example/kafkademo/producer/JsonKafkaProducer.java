package com.example.kafkademo.producer;

import com.example.kafkademo.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaProducer {

    private final KafkaTemplate<String, Customer> kafkaTemplate;
    private final String topicName;

    public JsonKafkaProducer(
            KafkaTemplate<String, Customer> kafkaTemplate,
            @Value("${app.kafka.customer-topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendMessage(Customer customer) {
        System.out.println("Producing Customer: " + customer);
        kafkaTemplate.send(topicName, customer);
    }
}
