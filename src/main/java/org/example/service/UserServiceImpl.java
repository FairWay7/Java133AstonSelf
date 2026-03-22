package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.dto.UserRequestDTO;
import org.example.model.dto.UserResponseDTO;
import org.example.model.dto.UserUpdateRequestDTO;
import org.example.model.entity.User;
import org.example.model.result.Result;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Result<UserResponseDTO> createUser(UserRequestDTO request) {
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

        if(userRepository.existsByEmail(request.email())) {
            logger.warn("Attempt to create user with existing email: {}", request.email());
            errors.add("Email already exists: " + request.email());
            return Result.failure(errors);
        }

        Optional<User> savedUser = userRepository.save(request.toEntity());
        logger.info("User created successfully with id: {}", savedUser.get().getId());
        return Result.success(UserResponseDTO.fromEntity(savedUser.get()));
    }

    @Override
    public Result<List<UserResponseDTO>> getAllUsers() {
        return (Result<List<UserResponseDTO>>) userRepository.findAll().stream()
            .map(UserResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public Result<UserResponseDTO> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.isPresent() ?
            Result.success(UserResponseDTO.fromEntity(user.get())) :
            Result.failure(new ArrayList<>(Arrays.asList("User not found by id")));
    }

    @Override
    public Result<UserResponseDTO> getUsersByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        return user.isPresent() ?
            Result.success(UserResponseDTO.fromEntity(user.get())) :
            Result.failure(new ArrayList<>(Arrays.asList("User not found by email")));
    }

    @Override
    public Result<UserResponseDTO> getUsersByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.isPresent() ?
            Result.success(UserResponseDTO.fromEntity(user.get())) :
            Result.failure(new ArrayList<>(Arrays.asList("User not found by username")));
    }

    @Override
    public Result<UserResponseDTO> updateUser(UserUpdateRequestDTO request) {
        List<String> errors = new ArrayList<>();
        Optional<User> user = userRepository.findById(request.id());

        if(user.isEmpty()) {
            errors.add("User not found by id");
            return Result.failure(errors);
        }

        if (!request.email().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            logger.warn("Email already in use: {}", request.email());
            errors.add("Email already in use: " + request.email());
            return Result.failure(errors);
        }

        request.applyTo(user.get());
        userRepository.update(user.get());

        logger.info("User updated successfully with id: {}", user.get().getId());
        return Result.success(UserResponseDTO.fromEntity(user.get()));
    }

    @Override
    public Result<Boolean> deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("User deleted successfully with id: {}", id);
            return Result.success(true);
        }
        logger.info("User not found by id: {}", id);
        return Result.failure(new ArrayList<>(Arrays.asList("User not found by id " + id)));
    }
}
