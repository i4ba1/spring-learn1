package com.dev.springlearn1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordEmail(String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("muhamadnizariqbal@outlook.com");
        message.setTo(toEmail);
        message.setSubject("Your Account Password");
        message.setText("Your account has been created. Your temporary password is: " + password +
                "\n\nPlease log in and change your password as soon as possible.");

        mailSender.send(message);
    }
}
