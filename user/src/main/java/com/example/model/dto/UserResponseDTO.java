package com.example.model.dto;

import com.example.model.entity.User;

import java.time.format.DateTimeFormatter;

public record UserResponseDTO(
    Long id,
    String username,
    String email,
    Integer age,
    String createdAt
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getAge(),
            user.getCreated_at().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

    public String toString() {
        return String.format("ID: %d; Имя: %s; Email: %s; Возраст: %d; Создан: %s",
            id, username, email, age, createdAt);
    }
}
