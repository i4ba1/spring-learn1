package com.dev.springlearn1.services;

import com.dev.springlearn1.dto.EmailMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailKafkaConsumer {

    private final JavaMailSender mailSender;

    public EmailKafkaConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "${kafka.topic.email}", groupId = "email-group")
    public void listen(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@yourdomain.com");
        message.setTo(emailMessage.getTo());
        message.setSubject(emailMessage.getSubject());
        message.setText(emailMessage.getBody());

        mailSender.send(message);
    }
}
