package org.example.hm2.dto;

import org.example.hm2.entity.User;

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
