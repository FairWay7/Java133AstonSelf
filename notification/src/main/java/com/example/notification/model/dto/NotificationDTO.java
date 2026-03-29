package com.example.notification.model.dto;

import com.example.notification.model.data.UserOperation;
import lombok.Data;
import lombok.Value;

public record NotificationDTO(
    UserOperation userOperation,
    String email) { }
