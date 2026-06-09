package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.model.RefreshToken;
import com.mohan.taskmanager.task_workflow_system.repository.RefreshTokenRepository;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {


    @Value("${jwt.refresh_expiration}")
    private long refreshExpiration;

    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(String userId) {

        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(
                token,
                userId,
                LocalDateTime.now().plusSeconds(refreshExpiration/1000),
                false
        );
        log.info(
                "Creating refresh token for userId={}",
                userId
        );
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(refreshToken.isRevoked()){
            log.info(
                    "Refresh token revoked"
            );
            throw new RuntimeException("Refresh token is revoked");
        }

        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            log.warn(
                    "Expired refresh token used"
            );
            throw new RuntimeException("Refresh token is expired");
        }

        return refreshToken;
    }

    @Override
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }




}
