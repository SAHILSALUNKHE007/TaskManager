package com.example.taskmanager.service;

import com.example.taskmanager.dto.EmailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailEventProducer {

    private  final KafkaTemplate<String, EmailDTO> kafkaTemplate;

    public void sendEmailEvent(EmailDTO emailDTO){
        kafkaTemplate.send("email_topic",emailDTO);
    }
}
