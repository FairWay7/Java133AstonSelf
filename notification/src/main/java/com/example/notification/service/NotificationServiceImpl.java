package com.example.notification.service;

import com.example.notification.model.data.UserOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);
    private final EmailService emailService;

    public NotificationServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendEmailNotification(String operation, String email) {
        logger.info("Sending email notification via API: operation={}, email={}",
            operation, email);

        if (UserOperation.CREATE.equals(operation)) {
            emailService.sendAccountCreationEmail(email);
        }
        else if (UserOperation.DELETE.equals(operation)) {
            emailService.sendAccountDeletionEmail(email);
        }
    }
}
