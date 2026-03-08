package org.example.hm2.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.hm2.dao.UserDAO;
import org.example.hm2.dao.UserDAOImpl;
import org.example.hm2.dto.UserRequestDTO;
import org.example.hm2.dto.UserResponseDTO;
import org.example.hm2.dto.UserUpdateRequestDTO;
import org.example.hm2.entity.User;
import org.example.hm2.exception.DuplicateEmailException;
import org.example.hm2.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) throws DuplicateEmailException {
        if(userDAO.getByEmail(request.email()).isPresent()) {
            logger.warn("Attempt to create user with existing email: {}", request.email());
            throw new DuplicateEmailException("Email already exists: " + request.email());
        }
        Optional<User> savedUser = userDAO.create(request.toEntity());
        logger.info("User created successfully with id: {}", savedUser.get().getId());
        return UserResponseDTO.fromEntity(savedUser.get());
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll().stream()
            .map(UserResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) throws UserNotFoundException {
        User user = userDAO.getById(id)
            .orElseThrow(() -> {
                logger.warn("User not found by id: {}", id);
                throw new UserNotFoundException("User not found by id: " + id);
            });

        return UserResponseDTO.fromEntity(user);
    }

    @Override
    public UserResponseDTO getUsersByEmail(String email) throws UserNotFoundException {
        User user = userDAO.getByEmail(email)
            .orElseThrow(() -> {
                logger.warn("User not found by id: {}", email);
                throw new UserNotFoundException("User not found by id: " + email);
            });

        return UserResponseDTO.fromEntity(user);
    }

    @Override
    public UserResponseDTO getUsersByUsername(String username) throws UserNotFoundException {
        User user = userDAO.getByUsername(username)
            .orElseThrow(() -> {
                logger.warn("User not found with username: {}", username);
                throw new UserNotFoundException("User not found with username: " + username);
            });

        return UserResponseDTO.fromEntity(user);
    }

    @Override
    public UserResponseDTO updateUser(UserUpdateRequestDTO request) throws UserNotFoundException,
        DuplicateEmailException {
        User user = userDAO.getById(request.id())
            .orElseThrow(() -> {
                logger.warn("User not found by id: {}", request.id());
                return new UserNotFoundException("User not found by id: " + request.id());
            });

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            Optional<User> existingUser = userDAO.getByEmail(request.email());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(request.id())) {
                logger.warn("Email already in use: {}", request.email());
                throw new DuplicateEmailException("Email already in use: " + request.email());
            }
        }

        request.applyTo(user);
        userDAO.update(user);

        logger.info("User updated successfully with id: {}", user.getId());
        return UserResponseDTO.fromEntity(user);
    }

    @Override
    public UserResponseDTO deleteUser(Long id) throws UserNotFoundException {
        User user = userDAO.getById(id)
            .orElseThrow(() -> {
                logger.warn("User not found with id: {}", id);
                throw new UserNotFoundException("User not found with id: " + id);
            });

        userDAO.deleteById(id);
        logger.info("User deleted successfully with id: {}", id);
        return UserResponseDTO.fromEntity(user);
    }
}
