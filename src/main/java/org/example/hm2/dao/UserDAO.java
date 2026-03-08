package org.example.hm2.dao;

import org.example.hm2.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> create(User user);
    List<User> findAll();
    Optional<User> getById(Long id);
    Optional<User> getByEmail(String email);
    Optional<User> getByUsername(String username);
    Optional<User> update(User user);
    boolean deleteById(Long id);
}
