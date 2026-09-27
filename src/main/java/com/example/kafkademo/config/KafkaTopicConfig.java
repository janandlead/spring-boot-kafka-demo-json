package com.example.kafkademo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    private final String customerTopic;

    /**
     * Creates the topic configuration using the topic name from application properties.
     *
     * @param customerTopic configured Kafka topic name
     */
    public KafkaTopicConfig(@Value("${app.kafka.customer-topic}") String customerTopic) {
        this.customerTopic = customerTopic;
    }

    /**
     * Defines the Kafka topic used for customer messages.
     *
     * @return a topic with one partition and one replica
     */
    @Bean
    public NewTopic customerTopic() {
        return TopicBuilder
                .name(customerTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
