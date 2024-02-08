package com.mete;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailingService {

    private final JavaMailSender javaMailSender;

    public void sendSimpleEmail(String to, String subject, String body) {
        System.out.println("method worked sendSimpleEmail");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        System.out.println("to= " + to + " subject= " + subject + " body= " + body);
        javaMailSender.send(message);
    }
}
