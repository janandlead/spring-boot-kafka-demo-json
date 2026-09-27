package com.example.kafkademo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    private final String customerTopic;

    public KafkaTopicConfig(@Value("${app.kafka.customer-topic}") String customerTopic) {
        this.customerTopic = customerTopic;
    }

    @Bean
    public NewTopic customerTopic() {
        return TopicBuilder
                .name(customerTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
