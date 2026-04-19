package com.example.user.service;

import com.example.user.model.dto.UserRequestDTO;
import com.example.user.model.dto.UserResponseDTO;
import com.example.user.model.dto.UserUpdateRequestDTO;
import com.example.user.model.result.Result;

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
