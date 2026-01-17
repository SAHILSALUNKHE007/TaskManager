package com.example.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
        private final JavaMailSender mailSender;

        @Async
        public void sendTaskReminder(String toMail,String subject,String body){
                SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
                simpleMailMessage.setTo(toMail);
                simpleMailMessage.setSubject(subject);
                simpleMailMessage.setText(body);
                mailSender.send(simpleMailMessage);
        }
}
