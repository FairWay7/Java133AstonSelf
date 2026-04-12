package com.example.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Schema(description = "Модель данных пользователя")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Уникальный идентификатор пользователя",
        example = "5010")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Имя пользователя",
        example = "IvanovII",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Column(nullable = false, unique = true)
    @Schema(description = "Электронный адрес пользователя",
        example = "ivanovii@yandex.ru",
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Возраст пользователя",
        example = "20",
        minimum = "0", maximum = "120")
    private int age;

    @Schema(description = "Дата создания объекта в системе")
    private LocalDateTime created_at;

    public User() {
    }

    public User(String username, String email, Integer age) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.created_at = LocalDateTime.now();
    }

    public User(String username, String email, Integer age, LocalDateTime created_at) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
