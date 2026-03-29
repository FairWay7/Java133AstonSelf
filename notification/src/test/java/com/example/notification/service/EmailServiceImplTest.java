package com.example.notification.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void testSendAccountCreationEmail() {
        emailService.sendAccountCreationEmail("test@example.com");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendAccountDeletionEmail() {
        emailService.sendAccountDeletionEmail("test@example.com");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
