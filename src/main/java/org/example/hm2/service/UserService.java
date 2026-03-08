package org.example.hm2.service;

import org.example.hm2.dto.UserRequestDTO;
import org.example.hm2.dto.UserResponseDTO;
import org.example.hm2.dto.UserUpdateRequestDTO;
import org.example.hm2.exception.DuplicateEmailException;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO request) throws DuplicateEmailException;
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUsersByEmail(String email);
    UserResponseDTO getUsersByUsername(String username);
    UserResponseDTO updateUser(UserUpdateRequestDTO request);
    UserResponseDTO deleteUser(Long id);
}
