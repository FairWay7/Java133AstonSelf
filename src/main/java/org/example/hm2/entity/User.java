package org.example.hm2.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private int age;
    private LocalDateTime created_at;

    public User() {}

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

    @Override
    public String toString() {
        return String.format("User{id=%s, name='%s', email='%s', age=%d, createdAt=%s}",
            id, username, email, age, created_at);
    }
}
