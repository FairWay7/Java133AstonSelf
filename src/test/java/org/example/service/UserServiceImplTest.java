package org.example.service;

import org.example.model.dto.UserRequestDTO;
import org.example.model.dto.UserResponseDTO;
import org.example.model.dto.UserUpdateRequestDTO;
import org.example.model.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.model.result.Result;
import org.example.repository.UserRepository;
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
        when(userRepository.save(any(User.class))).thenReturn(Optional.of(user));
        Result<UserResponseDTO> result = userService.createUser(new UserRequestDTO(username, email, age));

        assertTrue(result.isSuccess());

        UserResponseDTO resultUser = result.getData().get();
        assertEquals(username, resultUser.username());
        assertEquals(email, resultUser.email());
        assertEquals((Integer) age, resultUser.age());

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void createOldUser() {
        String username = "user1";
        String email = "user1@example.com";
        int age = 121;

        User user = new User(username, email, age);
        when(userRepository.save(any(User.class))).thenReturn(Optional.empty());

        Result<UserResponseDTO> result = userService.createUser(new UserRequestDTO(username, email, age));

        assertTrue(result.isFailure());
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void createUserWithEmptyUsername() {
        String username = "";
        String email = "user2@example.com";
        Integer age = 30;

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(new UserRequestDTO(username, email, age));
        });

        verify(userRepository, never()).save(any(User.class));
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

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUsersByEmail(email);
        });

        verify(userRepository).findByEmail(email);
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
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.of(user));

        Result<UserResponseDTO> result = userService.updateUser(new UserUpdateRequestDTO(userId, newName, newEmail, newAge));

        UserResponseDTO resultUser = result.getData().get();
        assertEquals(newName, resultUser.username());
        assertEquals(newEmail, resultUser.email());
        assertEquals(newAge, resultUser.age());

        verify(userRepository).findById(userId);
        verify(userRepository).findByEmail(newEmail);
        verify(userRepository).update(user);
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        User user = new User("old", "old@example.com", 21);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

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
