package com.example.taskmanager.service;

import com.example.taskmanager.dto.EmailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailEventConsumer {
        private final EmailService emailService;

        @KafkaListener(
                topics = "email_topic",
                groupId = "test-group"
        )
        public  void consume(EmailDTO emailDTO){
            StringBuilder body=new StringBuilder();
            body.append("You have pending tasks for today:\n\n");

            for(String task:emailDTO.getTaskList()){
                body.append("- ").append(task).append("\n");
            }
            body.append("\nPlease complete them on time.");

            emailService.sendTaskReminder(
                    emailDTO.getEmail(),
                    emailDTO.getSubject(),
                    body.toString()
            );
        }
}
