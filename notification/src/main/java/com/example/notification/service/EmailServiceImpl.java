package com.example.notification.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.fromEmail = "denisgm2003@gmail.com";
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
