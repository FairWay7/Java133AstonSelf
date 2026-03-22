package org.example.service;

import org.example.model.dto.UserRequestDTO;
import org.example.model.dto.UserResponseDTO;
import org.example.model.dto.UserUpdateRequestDTO;
import org.example.exception.DuplicateEmailException;
import org.example.model.result.Result;

import java.util.List;

public interface UserService {
    Result<UserResponseDTO> createUser(UserRequestDTO request);
    Result<List<UserResponseDTO>> getAllUsers();
    Result<UserResponseDTO> getUserById(Long id);
    Result<UserResponseDTO> getUsersByEmail(String email);
    Result<UserResponseDTO> getUsersByUsername(String username);
    Result<UserResponseDTO> updateUser(UserUpdateRequestDTO request);
    Result<Boolean> deleteUser(Long id);
}
