package com.example.taskmanager.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TestConsumer {
    @KafkaListener(
            topics = "email_topic",
            groupId = "test-group"
    )
    public void listen(String message) {
        System.out.println("Received from Kafka: " + message);
    }
}
