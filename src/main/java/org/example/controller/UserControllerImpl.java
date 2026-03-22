package org.example.webapp.controller;

import org.example.webapp.exception.UserCreateException;
import org.example.webapp.exception.UserNotFoundException;
import org.example.webapp.model.dto.UserRequestDTO;
import org.example.webapp.model.dto.UserResponseDTO;
import org.example.webapp.model.dto.UserUpdateRequestDTO;
import org.example.webapp.model.entity.User;
import org.example.webapp.model.result.Result;
import org.example.webapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserControllerImpl implements  UserController {
    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO request) {
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
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id).getData()
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
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
            return ResponseEntity.ok(result.getData());
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
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Result<Boolean> deleted = userService.deleteUser(id);

        if (deleted.isSuccess()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
