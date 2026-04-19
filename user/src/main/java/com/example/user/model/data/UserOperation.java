package com.example.user.model.data;

public enum UserOperation {
    CREATE("Создание пользователя"),
    DELETE("Удаление пользователя"),
    UPDATE("Обновление пользователя");

    private final String description;

    UserOperation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
