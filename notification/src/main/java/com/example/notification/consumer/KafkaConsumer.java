package com.example.notification.consumer;

import com.example.notification.model.dto.NotificationDTO;
import com.example.notification.service.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumer {
    private static final Logger logger = LogManager.getLogger(KafkaConsumer.class);
    private static final String TOPIC = "t.notification";
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public KafkaConsumer(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = TOPIC, groupId = "my-group")
    public void listen(String message) {
        logger.info("Received message: {}", message);
        NotificationDTO not = objectMapper.readValue(message, NotificationDTO.class);
        notificationService.sendEmailNotification(not.userOperation().toString(), not.email());
    }
}
