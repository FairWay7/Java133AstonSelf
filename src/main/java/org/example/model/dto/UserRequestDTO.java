package org.example.webapp.model.dto;

import org.example.webapp.model.entity.User;

public record UserRequestDTO(
    String username,
    String email,
    Integer age
) {
    public User toEntity() {
        return new User(username, email, age);
    }
}
