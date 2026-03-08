package org.example.hm2.dto;

import org.example.hm2.entity.User;

import java.time.format.DateTimeFormatter;

public record UserRequestDTO(
    String username,
    String email,
    Integer age
) {
    public User toEntity() {
        return new User(username, email, age);
    }
}
