package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.dto.request.UserRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.UserResponseDTO;
import com.mohan.taskmanager.task_workflow_system.exception.UserAlreadyExistsException;
import com.mohan.taskmanager.task_workflow_system.exception.UserNotFoundException;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUserSuccessfully() {

        UserRequestDTO dto = new UserRequestDTO();

        dto.setName("Mohan");
        dto.setEmail("mohan@gmail.com");
        dto.setPassword("password123");

        when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        UserResponseDTO response = userService.createUser(dto);

        assertNotNull(response);

        assertEquals("Mohan", response.name());

        assertEquals("mohan@gmail.com", response.email());

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        UserRequestDTO dto = new UserRequestDTO();

        dto.setEmail("mohan@gmail.com");

        User existingUser = new User();

        when(userRepository.findByEmail("mohan@gmail.com"))
                .thenReturn(Optional.of(existingUser));

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(dto)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(id)
        );
    }

    @Test
    void shouldReturnUserWhenUserExists() {

        UUID id = UUID.randomUUID();

        User user = new User();

        user.setUserId(id);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(id);

        assertEquals(id, result.getUserId());

        verify(userRepository)
                .findById(id);
    }

}