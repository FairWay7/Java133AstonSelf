package org.example.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.model.entity.User;

@Schema(description = "Запрос на обновление пользователя")
public record UserUpdateRequestDTO(
    @NotBlank
    @Schema(description = "Идентификатор пользователя")
    Long id,

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
    @Override
    public boolean isValid() {
        return false;
    }

    public User toEntity() {
        return new User(username, email, age);
    }

    public void applyTo(User user) {
        if (username != null) user.setUsername(username);
        if (email != null) user.setEmail(email);
        if (age != null) user.setAge(age);
    }
}
