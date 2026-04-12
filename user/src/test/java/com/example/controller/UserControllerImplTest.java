package com.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.model.dto.UserRequestDTO;
import com.example.model.dto.UserResponseDTO;
import com.example.model.entity.User;
import com.example.model.result.Result;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerImplTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerImpl userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(userController)
            .build();

        objectMapper = new ObjectMapper();

        testUser = new User("example-user", "example-user@example.com", 40);
        testUser.setId(1L);
    }

    @Test
    void getAllUsers() throws Exception {
        List<UserResponseDTO> users = Arrays.asList(UserResponseDTO.fromEntity(testUser));
        when(userService.getAllUsers()).thenReturn(Result.success(users));

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].username").value("example-user"));
    }

    @Test
    void getUserById_WhenUserExists() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Result.success(UserResponseDTO.fromEntity(testUser)));

        mockMvc.perform(get("/api/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("example-user"));
    }

    @Test
    void getUserByEmail_WhenUserExists() throws Exception {
        when(userService.getUsersByEmail("example-user@example.com")).thenReturn(Result.success(UserResponseDTO.fromEntity(testUser)));

        mockMvc.perform(get("/api/users/email/{email}", "example-user@example.com")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("example-user@example.com"));
    }

    @Test
    void createUser() throws Exception {
        User savedUser = new User("example-user","example-user@example.com", 33);
        savedUser.setId(2L);
        UserRequestDTO userRequestDTO = new UserRequestDTO(
            savedUser.getUsername(), savedUser.getEmail(), savedUser.getAge()
        );

        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(
            Result.success(UserResponseDTO.fromEntity(savedUser))
        );

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.username").value("example-user"))
            .andExpect(jsonPath("$.email").value("example-user@example.com"));
    }


    @Test
    void deleteUser() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(Result.success(true));

        mockMvc.perform(delete("/api/users/{id}", 1L))
            .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
