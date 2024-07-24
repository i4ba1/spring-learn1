package com.dev.springlearn1.services;

import com.dev.springlearn1.config.KafkaProperties;
import com.dev.springlearn1.dto.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AsyncEmailService {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Autowired
    public AsyncEmailService(KafkaTemplate<String, EmailMessage> kafkaTemplate, KafkaProperties kafkaProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProperties = kafkaProperties;
    }

    public void sendPasswordEmail(String toEmail, String password) {
        String subject = "Your Account Password";
        String body = "Your account has been created. Your temporary password is: " + password +
                "\n\nFor security reasons, please log in and change your password immediately." +
                "\n\nDo not share this password with anyone.";

        EmailMessage emailMessage = new EmailMessage(toEmail, subject, body);
        kafkaTemplate.send(kafkaProperties.getTopics().getEmail(), emailMessage);
    }
}
