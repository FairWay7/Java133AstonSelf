package org.example.model.dto;

import org.example.model.entity.User;

public record UserRequestDTO(
    String username,
    String email,
    Integer age
) implements RequestDTO {
    public User toEntity() {
        return new User(username, email, age);
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
