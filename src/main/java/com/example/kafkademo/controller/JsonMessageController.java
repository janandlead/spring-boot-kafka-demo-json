package com.example.kafkademo.controller;

import com.example.kafkademo.model.Customer;
import com.example.kafkademo.producer.JsonKafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kafka")
public class JsonMessageController {

    private final JsonKafkaProducer kafkaProducer;

    public JsonMessageController(JsonKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish-customer")
    public ResponseEntity<String> publishCustomer(@RequestBody Customer customer) {
        kafkaProducer.sendMessage(customer);
        return ResponseEntity.ok("Customer sent to Kafka successfully");
    }
}
