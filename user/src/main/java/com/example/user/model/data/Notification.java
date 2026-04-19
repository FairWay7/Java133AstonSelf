package com.example.user.model.data;

import lombok.Data;
import lombok.Value;

import java.util.Objects;

public class Notification {
    private UserOperation userOperation;
    private String email;

    public Notification() {
    }

    public Notification(UserOperation userOperation, String email) {
        this.userOperation = userOperation;
        this.email = email;
    }

    public UserOperation getUserOperation() {
        return userOperation;
    }

    public void setUserOperation(UserOperation userOperation) {
        this.userOperation = userOperation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return userOperation == that.userOperation && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userOperation, email);
    }

    @Override
    public String toString() {
        return "Notification{" +
            "userOperation=" + userOperation +
            ", email='" + email + '\'' +
            '}';
    }
}
