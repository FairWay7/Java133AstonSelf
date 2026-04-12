package com.example.service;

import com.example.model.dto.UserRequestDTO;
import com.example.model.dto.UserResponseDTO;
import com.example.model.dto.UserUpdateRequestDTO;
import com.example.model.result.Result;

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
