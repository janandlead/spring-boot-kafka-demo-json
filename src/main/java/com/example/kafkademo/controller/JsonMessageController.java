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

    /**
     * Creates the controller with its Kafka producer dependency.
     *
     * @param kafkaProducer service used to publish customer messages
     */
    public JsonMessageController(JsonKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Publishes a customer received from an HTTP POST request.
     *
     * @param customer customer parsed from the request JSON body
     * @return confirmation response after the message is submitted to Kafka
     */
    @PostMapping("/publish-customer")
    public ResponseEntity<String> publishCustomer(@RequestBody Customer customer) {
        kafkaProducer.sendMessage(customer);
        return ResponseEntity.ok("Customer sent to Kafka successfully");
    }
}
