package com.example.notification.service;

public interface EmailService {
    void sendAccountCreationEmail(String toEmail);
    void sendAccountDeletionEmail(String toEmail);
}
