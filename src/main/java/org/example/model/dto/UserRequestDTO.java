package org.example.model.dto;

import org.example.model.entity.User;

public record UserRequestDTO(
    String username,
    String email,
    Integer age
) {
    public User toEntity() {
        return new User(username, email, age);
    }
}
