package com.example.demo.utils;

import com.example.demo.entity.Email;
import groovy.util.logging.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendEmail {
    @Autowired
    private JavaMailSender javaMailSender;

    private static final Logger logger = LogManager.getLogger(SendEmail.class);

    public boolean doSendEmail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getSentTo());
        message.setSubject(email.getSubject());
        message.setText(email.getTextContent());
        JavaMailSender javaMailSender1 = new JavaMailSenderImpl();
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.throwing(e);
            return false;
        }
        return true;
    }
}
