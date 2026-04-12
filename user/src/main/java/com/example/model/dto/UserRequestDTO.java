package com.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.example.model.entity.User;

@Schema(description = "Запрос на создание пользователя")
public record UserRequestDTO(
    @NotBlank
    @Size(min = 3, max = 60)
    @Schema(description = "Имя пользователя")
    String username,

    @NotBlank
    @Email
    @Schema(description = "Email пользователя")
    String email,

    @NotBlank
    @Size(min = 3, max = 120)
    @Schema(description = "Возраст пользователя")
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
