package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.KafkaConfig;
import org.example.model.data.Notification;
import org.example.model.data.UserOperation;
import org.example.model.dto.UserRequestDTO;
import org.example.model.dto.UserResponseDTO;
import org.example.model.dto.UserUpdateRequestDTO;
import org.example.model.entity.User;
import org.example.model.result.Result;
import org.example.producer.KafkaProducer;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final KafkaProducer kafkaProducer;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        KafkaConfig kafkaConfig = new KafkaConfig();
        this.kafkaProducer = new KafkaProducer(kafkaConfig.kafkaTemplate(), kafkaConfig.objectMapper());
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository, KafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public Result<UserResponseDTO> createUser(UserRequestDTO request) {
        List<String> errors = new ArrayList<>();

        if(userRepository.existsByEmail(request.email())) {
            logger.warn("Attempt to create user with existing email: {}", request.email());
            errors.add("Email already exists: " + request.email());
            return Result.failure(errors);
        }

        User savedUser = userRepository.save(request.toEntity());

        if(savedUser == null) {
            logger.info("UserRepository did not save the user with email: {}", request.email());
            errors.add("UserRepository did not save the user with email: " + request.email());
            return Result.failure(errors);
        }

        logger.info("User created successfully with email: {}", savedUser.getEmail());

        kafkaProducer.sendMessage(new Notification(UserOperation.CREATE,savedUser.getEmail()));

        return Result.success(UserResponseDTO.fromEntity(savedUser));
    }

    @Override
    public Result<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userRepository.findAll().stream()
            .map(UserResponseDTO::fromEntity)
            .collect(Collectors.toList());

        return Result.success(users);
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

        if (!request.email().equals(user.get().getEmail()) && userRepository.existsByEmail(request.email())) {
            logger.warn("Email already in use: {}", request.email());
            errors.add("Email already in use: " + request.email());
            return Result.failure(errors);
        }

        request.applyTo(user.get());
        userRepository.save(user.get());

        logger.info("User updated successfully with id: {}", user.get().getId());
        return Result.success(UserResponseDTO.fromEntity(user.get()));
    }

    @Override
    public Result<Boolean> deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            Optional<User> user = userRepository.findById(id);
            userRepository.deleteById(id);
            logger.info("User deleted successfully with id: {}", id);

            kafkaProducer.sendMessage(new Notification(UserOperation.DELETE, user.get().getEmail()));

            return Result.success(true);
        }
        logger.info("User not found by id: {}", id);
        return Result.failure(new ArrayList<>(Arrays.asList("User not found by id " + id)));
    }
}
