package com.example.notification.controller;


import com.example.notification.model.dto.NotificationDTO;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendEmailNotification(@RequestBody NotificationDTO request) {
        notificationService.sendEmailNotification(
            request.userOperation().toString(), request.email()
        );
        return ResponseEntity.ok().build();
    }
}
