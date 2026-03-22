package org.example.webapp.service;

import org.example.webapp.dao.UserDAO;
import org.example.webapp.model.dto.UserRequestDTO;
import org.example.webapp.model.dto.UserResponseDTO;
import org.example.webapp.model.dto.UserUpdateRequestDTO;
import org.example.webapp.model.entity.User;
import org.example.webapp.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    public void createUser() {
        String username = "user1";
        String email = "user1@example.com";
        int age = 20;

        User user = new User(username, email, age);
        when(userDAO.create(any(User.class))).thenReturn(Optional.of(user));
        UserResponseDTO result = userService.createUser(new UserRequestDTO(username, email, age));

        assertNotNull(result);
        assertEquals(username, result.username());
        assertEquals(email, result.email());
        assertEquals((Integer) age, result.age());

        verify(userDAO).findByEmail(email);
        verify(userDAO).create(any(User.class));
    }

    @Test
    public void createOldUser() {
        String username = "user1";
        String email = "user1@example.com";
        int age = 121;

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(new UserRequestDTO(username, email, age));
        });

        verify(userDAO, never()).create(any(User.class));
    }

    @Test
    void createUserWithEmptyEmail() {
        String username = "user1";
        String email = "";
        Integer age = 30;

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(new UserRequestDTO(username, email, age));
        });

        verify(userDAO, never()).create(any(User.class));
    }

    @Test
    void createUserWithEmptyUsername() {
        String username = "";
        String email = "user2@example.com";
        Integer age = 30;

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(new UserRequestDTO(username, email, age));
        });

        verify(userDAO, never()).create(any(User.class));
    }

    @Test
    void getUserByEmail() {
        String email = "user2@example.com";
        User expectedUser = new User("Denis", email, 30);
        expectedUser.setId(1L);

        when(userDAO.findByEmail(email)).thenReturn(Optional.of(expectedUser));
        UserResponseDTO result = userService.getUsersByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.email());
        assertEquals("Denis", result.username());

        verify(userDAO).findByEmail(email);
    }

    @Test
    void getUserByEmail1() {
        String email = "user1242@example.com";

        when(userDAO.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUsersByEmail(email);
        });

        verify(userDAO).findByEmail(email);
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        User user = new User("old", "old@example.com", 21);
        user.setId(userId);

        String newName = "newName";
        String newEmail = "new@example.com";
        Integer newAge = 30;

        when(userDAO.findById(userId)).thenReturn(Optional.of(user));
        when(userDAO.findByEmail(newEmail)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.updateUser(new UserUpdateRequestDTO(userId, newName, newEmail, newAge));

        assertEquals(newName, result.username());
        assertEquals(newEmail, result.email());
        assertEquals(newAge, result.age());

        verify(userDAO).findById(userId);
        verify(userDAO).findByEmail(newEmail);
        verify(userDAO).update(user);
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        User user = new User("old", "old@example.com", 21);
        user.setId(userId);

        when(userDAO.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userDAO).findById(userId);
        verify(userDAO).deleteById(user.getId());
    }

    @Test
    void getAllUsers() {
        User user1 = new User("old1", "old3@example.com", 24);
        User user2 = new User("old2", "old4@example.com", 21);

        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userDAO.findAll()).thenReturn(expectedUsers);

        List<UserResponseDTO> actualUsers = userService.getAllUsers();

        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());

        verify(userDAO, times(1)).findAll();
        verifyNoMoreInteractions(userDAO);
    }
}
