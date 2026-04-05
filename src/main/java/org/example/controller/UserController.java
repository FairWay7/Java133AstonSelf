package org.example.controller;

import org.example.model.dto.UserRequestDTO;
import org.example.model.dto.UserResponseDTO;
import org.example.model.dto.UserUpdateRequestDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserController {
    ResponseEntity<?> createUser(UserRequestDTO request);

    ResponseEntity<EntityModel<UserResponseDTO>> getUserById(Long id);

    ResponseEntity<EntityModel<UserResponseDTO>> getUserByEmail(String email);

    ResponseEntity<List<EntityModel<UserResponseDTO>>> getAllUsers();

    ResponseEntity<?> updateUser(UserUpdateRequestDTO request);

    ResponseEntity<?> deleteUser(Long id);
}
