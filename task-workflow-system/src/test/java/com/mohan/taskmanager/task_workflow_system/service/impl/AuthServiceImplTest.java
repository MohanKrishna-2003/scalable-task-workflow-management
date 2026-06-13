package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.config.security.JwtService;
import com.mohan.taskmanager.task_workflow_system.dto.response.LoginResponseDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.RefreshTokenResponseDTO;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.repository.UserRepository;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.RefreshTokenService;
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
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldLoginSuccessfully() {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setUserId(userId);
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("access-token");

        // just verify interaction, no need to mock return value
        doNothing().when(refreshTokenService)
                .createRefreshToken(userId.toString());

        LoginResponseDTO response =
                authService.login("test@gmail.com", "password");

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());

        verify(refreshTokenService)
                .createRefreshToken(userId.toString());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> authService.login("test@gmail.com", "password"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidPassword() {

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "encodedPassword"))
                .thenReturn(false);

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> authService.login("test@gmail.com", "password"));

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void shouldRefreshTokenSuccessfully() {

        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setUserId(userId);

        when(refreshTokenService.validateRefreshToken("old-token"))
                .thenReturn(userId.toString());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(user))
                .thenReturn("new-access-token");

        doNothing().when(refreshTokenService)
                .createRefreshToken(userId.toString());

        RefreshTokenResponseDTO response =
                authService.refreshToken("old-token");

        assertEquals("new-access-token", response.getAccessToken());

        verify(refreshTokenService)
                .revokeRefreshToken("old-token");
    }

    @Test
    void shouldLogoutSuccessfully() {

        authService.logout("refresh-token");

        verify(refreshTokenService)
                .revokeRefreshToken("refresh-token");
    }
}