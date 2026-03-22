package org.example.webapp.model.dto;

import org.example.webapp.model.entity.User;

public record UserUpdateRequestDTO(
    Long id,
    String username,
    String email,
    Integer age
) {
    public User toEntity() {
        return new User(username, email, age);
    }

    public void applyTo(User user) {
        if (username != null) user.setUsername(username);
        if (email != null) user.setEmail(email);
        if (age != null) user.setAge(age);
    }
}
