package com.example.notification.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Value("${from.email}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAccountCreationEmail(String toEmail) {
        String text = String.format("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");
        sendEmail(toEmail, text);
    }

    public void sendAccountDeletionEmail(String toEmail) {
        String text = "Здравствуйте! Ваш аккаунт был удалён.";
        sendEmail(toEmail, text);
    }

    private void sendEmail(String toEmail, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setText(text);

            mailSender.send(message);
            logger.info("Email sent successfully to: {}", toEmail);
        }
        catch (Exception e) {
            logger.error("Failed to send email to: {}", toEmail, e);
        }
    }
}
