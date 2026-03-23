package org.example.controller;

import org.example.exception.UserCreateException;
import org.example.exception.UserNotFoundException;
import org.example.model.dto.RequestDTO;
import org.example.model.dto.UserRequestDTO;
import org.example.model.dto.UserResponseDTO;
import org.example.model.dto.UserUpdateRequestDTO;
import org.example.model.result.Result;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserControllerImpl implements UserController {
    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO request) {
        List<String> errors = validateParams(request);
        if(!errors.isEmpty()) {
            ErrorResponse error = ErrorResponse.create(
                new UserCreateException(""),
                HttpStatus.BAD_REQUEST,
                errors.toString()
            );
            return ResponseEntity.badRequest().body(error);
        }

        Result<UserResponseDTO> result = userService.createUser(request);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getData().get());
        }
        else {
            ErrorResponse error = ErrorResponse.create(
                new UserCreateException(""),
                HttpStatus.BAD_REQUEST,
                result.getErrors().toString()
            );
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id).getData()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable("email") String email) {
        return userService.getUsersByEmail(email).getData()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers().getData().get();
        return ResponseEntity.ok(users);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequestDTO request) {
        Result<UserResponseDTO> result = userService.updateUser(request);

        if (result.isSuccess()) {
            return ResponseEntity.ok(true);
        }
        else {
            ErrorResponse error = ErrorResponse.create(
                new UserCreateException(""),
                HttpStatus.BAD_REQUEST,
                result.getErrors().toString()
            );
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        Result<Boolean> deleted = userService.deleteUser(id);

        if (deleted.isSuccess()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private List<String> validateParams(UserRequestDTO request) {
        List<String> errors = new ArrayList<>();

        if (request.username() == null || request.username().trim().isEmpty()) {
            errors.add("Invalid username format");
        }

        if (request.age() < 8 || request.age() > 120) {
            errors.add("Invalid age format");
        }

        if (request.email() == null || request.email().trim().isEmpty()) {
            errors.add("Invalid email format");
        }

        return errors;
    }
}
