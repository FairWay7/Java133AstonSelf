package com.example.service;

import com.example.model.dto.UserRequestDTO;
import com.example.model.dto.UserResponseDTO;
import com.example.model.dto.UserUpdateRequestDTO;
import com.example.model.entity.User;
import com.example.model.result.Result;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void createUser() {
        String username = "user1";
        String email = "user1@example.com";
        int age = 20;

        User user = new User(username, email, age);
        when(userRepository.save(any(User.class))).thenReturn(user);
        Result<UserResponseDTO> result = userService.createUser(new UserRequestDTO(username, email, age));

        assertTrue(result.isSuccess());

        UserResponseDTO resultUser = result.getData().get();
        assertEquals(username, resultUser.username());
        assertEquals(email, resultUser.email());
        assertEquals((Integer) age, resultUser.age());

        verify(userRepository).existsByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserByEmail() {
        String email = "user2@example.com";
        User expectedUser = new User("Denis", email, 30);
        expectedUser.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));
        Result<UserResponseDTO> result = userService.getUsersByEmail(email);

        assertTrue(result.isSuccess());
        UserResponseDTO resultUser = result.getData().get();
        assertEquals(email, resultUser.email());
        assertEquals("Denis", resultUser.username());

        verify(userRepository).findByEmail(email);
    }

    @Test
    void getUserByEmail1() {
        String email = "user1242@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Result<UserResponseDTO> result = userService.getUsersByEmail(email);

        assertFalse(result.isSuccess());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void getUserById_whenOk() {
        Long id = 1L;
        String username = "Denis";
        String email = "user2@example.com";
        int age = 30;
        User expectedUser = new User(username, email, age);
        expectedUser.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));
        Result<UserResponseDTO> result = userService.getUserById(id);

        assertTrue(result.isSuccess());

        UserResponseDTO userDTO = result.getData().get();
        assertEquals(userDTO.username(), username);
        assertEquals(userDTO.email(), email);
        assertEquals(userDTO.age(), age);
        verify(userRepository).findById(id);
    }

    @Test
    void getUserById_whenBad() {
        Long id = 1L;
        User expectedUser = new User("Denis", "user2@example.com", 30);
        expectedUser.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.empty());
        Result<UserResponseDTO> result = userService.getUserById(id);

        assertFalse(result.isSuccess());
        assertFalse(result.getData().isPresent());
        verify(userRepository).findById(id);
    }

    @Test
    void getUsersByUsername_whenOk() {
        Long id = 1L;
        String username = "Denis";
        String email = "user2@example.com";
        int age = 30;
        User expectedUser = new User(username, email, age);
        expectedUser.setId(id);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));
        Result<UserResponseDTO> result = userService.getUsersByUsername(username);

        assertTrue(result.isSuccess());

        UserResponseDTO userDTO = result.getData().get();
        assertEquals(userDTO.username(), username);
        assertEquals(userDTO.email(), email);
        assertEquals(userDTO.age(), age);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        User user = new User("old", "old@example.com", 21);
        user.setId(userId);

        String newName = "newName";
        String newEmail = "new@example.com";
        Integer newAge = 30;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Result<UserResponseDTO> result = userService.updateUser(
            new UserUpdateRequestDTO(userId, newName, newEmail, newAge)
        );

        UserResponseDTO resultUser = result.getData().get();
        assertEquals(newName, resultUser.username());
        assertEquals(newEmail, resultUser.email());
        assertEquals(newAge, resultUser.age());

        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmail(newEmail);
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        User user = new User("old", "old@example.com", 21);
        user.setId(userId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void getAllUsers() {
        User user1 = new User("old1", "old3@example.com", 24);
        User user2 = new User("old2", "old4@example.com", 21);

        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        Result<List<UserResponseDTO>> actualUsers = userService.getAllUsers();
        assertTrue(actualUsers.isSuccess());

        List<UserResponseDTO> resultUsers = actualUsers.getData().get();
        assertNotNull(actualUsers);
        assertEquals(2, resultUsers.size());

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }
}
