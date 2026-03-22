package org.example.webapp.service;

import org.example.webapp.model.dto.UserRequestDTO;
import org.example.webapp.model.dto.UserResponseDTO;
import org.example.webapp.model.dto.UserUpdateRequestDTO;
import org.example.webapp.exception.DuplicateEmailException;
import org.example.webapp.model.result.Result;

import java.util.List;

public interface UserService {
    Result<UserResponseDTO> createUser(UserRequestDTO request) throws DuplicateEmailException;
    Result<List<UserResponseDTO>> getAllUsers();
    Result<UserResponseDTO> getUserById(Long id);
    Result<UserResponseDTO> getUsersByEmail(String email);
    Result<UserResponseDTO> getUsersByUsername(String username);
    Result<UserResponseDTO> updateUser(UserUpdateRequestDTO request);
    Result<Boolean> deleteUser(Long id);
}
