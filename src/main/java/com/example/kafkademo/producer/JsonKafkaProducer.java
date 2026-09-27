package com.example.kafkademo.producer;

import com.example.kafkademo.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaProducer {

    private final KafkaTemplate<String, Customer> kafkaTemplate;
    private final String topicName;

    /**
     * Creates the producer with Kafka and topic dependencies.
     *
     * @param kafkaTemplate template used to send customer messages
     * @param topicName configured Kafka topic name
     */
    public JsonKafkaProducer(
            KafkaTemplate<String, Customer> kafkaTemplate,
            @Value("${app.kafka.customer-topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    /**
     * Sends a customer to Kafka. Spring Kafka converts the object to JSON
     * using the configured JsonSerializer.
     *
     * @param customer customer object to publish
     */
    public void sendMessage(Customer customer) {
        System.out.println("Producing Customer: " + customer);
        kafkaTemplate.send(topicName, customer);
    }
}
