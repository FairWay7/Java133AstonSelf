package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.util.UserResponseAssembler;
import org.example.exception.UserCreateException;
import org.example.exception.UserNotFoundException;
import org.example.model.dto.*;
import org.example.model.result.Result;
import org.example.service.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "API управления данными пользователей (user)")
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final UserResponseAssembler userResponseAssembler;

    public UserControllerImpl(UserService userService, UserResponseAssembler userResponseAssembler) {
        this.userService = userService;
        this.userResponseAssembler = userResponseAssembler;
    }

    @Operation(summary = "Создать нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Неверные входные данные")
    @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует")
    @PostMapping
    public ResponseEntity<EntityModel<UserResponseDTO>> createUser(@RequestBody UserRequestDTO request) {
        List<String> errors = validateParams(request);
        if (!errors.isEmpty()) {
            throw new UserCreateException("Validation failed: " + errors.toString());
        }

        Result<UserResponseDTO> result = userService.createUser(request);

        if (result.isSuccess()) {
            UserResponseDTO user = result.getData().get();
            EntityModel<UserResponseDTO> userModel = userResponseAssembler.toModel(user);

            return ResponseEntity
                .created(linkTo(methodOn(UserControllerImpl.class).getUserById(user.id())).toUri())
                .body(userModel);
        }
        else {
            throw new UserCreateException("Failed to create user: " + result.getErrors().toString());
        }
    }

    @Operation(
        summary = "Получить данные пользователя по ID",
        description = "Возвращает объект пользователя, если он найден в системе.")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDTO>> getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id).getData()
            .map(userResponseAssembler::toModel)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Operation(
        summary = "Получить данные пользователя по Email",
        description = "Возвращает объект пользователя, если он найден в системе.")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @GetMapping("/email/{email}")
    public ResponseEntity<EntityModel<UserResponseDTO>> getUserByEmail(@PathVariable("email") String email) {
        return userService.getUsersByEmail(email).getData()
            .map(userResponseAssembler::toModelWithEmailLink)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Operation(
        summary = "Получить список всех пользователей",
        description = "Возвращает список пользователей.")
    @ApiResponse(responseCode = "200", description = "Список пользователей получен")
    @ApiResponse(responseCode = "204", description = "Нет пользователей")
    @GetMapping
    public ResponseEntity<List<EntityModel<UserResponseDTO>>> getAllUsers() {
        List<EntityModel<UserResponseDTO>> users = userService
            .getAllUsers().getData().get()
            .stream()
            .map(userResponseAssembler::toModel)
            .collect(Collectors.toList());;
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Обновить данные пользователя")
    @ApiResponse(responseCode = "201", description = "Данные пользователя успешно обновлены.")
    @ApiResponse(responseCode = "400", description = "Неверные входные данные")
    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequestDTO request) {
        Result<UserResponseDTO> result = userService.updateUser(request);

        if (result.isSuccess()) {
            UserUpdateResponseDTO response = new UserUpdateResponseDTO(true,
                "User updated successfully");

            EntityModel<UserUpdateResponseDTO> responseModel = EntityModel.of(response);
            responseModel.add(linkTo(methodOn(UserControllerImpl.class).getUserById(request.id()))
                .withRel("user"));
            responseModel.add(linkTo(methodOn(UserControllerImpl.class).getUserByEmail(request.email()))
                .withRel("email"));

            return ResponseEntity.ok(responseModel);
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

    @Operation(summary = "Удалить данные пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь удален")
    @ApiResponse(responseCode = "400", description = "Неверные входные данные")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDTO> deleteUser(@PathVariable("id") Long id) {
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
