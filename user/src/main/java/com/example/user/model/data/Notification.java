package com.example.user.model.data;

import lombok.Data;
import lombok.Value;

@Data
@Value
public class Notification {
    UserOperation userOperation;
    String email;
}
