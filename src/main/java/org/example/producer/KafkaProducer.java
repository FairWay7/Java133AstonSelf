package org.example.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.data.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private static final Logger logger = LogManager.getLogger(KafkaProducer.class);
    private static final String TOPIC = "t.notification";

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public boolean sendMessage(Notification notification) {
        try {
            String orderAsMessage = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(TOPIC, orderAsMessage);

            logger.info("Notification produced {}", orderAsMessage);
        }
        catch (Exception e) {
            logger.error("Notification not produced {}", e.getMessage());
            return false;
        }
        return true;
    }
}
