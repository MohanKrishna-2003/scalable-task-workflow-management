package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.config.security.JwtService;
import com.mohan.taskmanager.task_workflow_system.dto.response.LoginResponseDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.RefreshTokenResponseDTO;
import com.mohan.taskmanager.task_workflow_system.exception.InvalidCredentialsException;
import com.mohan.taskmanager.task_workflow_system.exception.UserNotFoundException;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.repository.UserRepository;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenServiceImpl refreshTokenService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenServiceImpl refreshTokenService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public LoginResponseDTO login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            log.warn(
                    "Failed login attempt email={}",
                    email
            );
            throw new RuntimeException("Invalid credentials");
        }

        // 1. Generate JWT token
        String accessToken = jwtService.generateToken(user);

        // 2. Generate Refresh Token
        String refreshToken = refreshTokenService.createRefreshToken(String.valueOf(user.getUserId()));

        log.info(
                "User authenticated successfully email={}",
                email
        );
        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(String token){

        // 1. validate the refresh token
        String oldRefreshToken = refreshTokenService.validateRefreshToken(token);

        // 2. Find user
        User user = userRepository.findById(UUID.fromString(oldRefreshToken))
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // 3. Generate NEW access token
        String newAccessToken = jwtService.generateToken(user);

        // 4. Generate NEW refresh token (ROTATION)
        String newRefreshToken =
                refreshTokenService.createRefreshToken(String.valueOf(user.getUserId()));

        // 5. Revoke OLD refresh token
        refreshTokenService.revokeRefreshToken(token);

        log.info(
                "Refreshing access token userId={}",
                user.getUserId()
        );
        // 6. Return new tokens
        return new RefreshTokenResponseDTO(
                newAccessToken,
                newRefreshToken
        );

    }

    @Override
    public void logout(String refreshToken) {
        log.info(
                "User logged out"
        );
        // revoke refresh token
        refreshTokenService.revokeRefreshToken(refreshToken);
    }
}
