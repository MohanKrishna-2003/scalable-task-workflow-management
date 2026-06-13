package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.config.security.JwtProperties;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.RefreshTokenService;
import com.mohan.taskmanager.task_workflow_system.service.redis.RedisRefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {


    private final JwtProperties jwtProperties;

    private final RedisRefreshTokenService redisService;

    public RefreshTokenServiceImpl(JwtProperties jwtProperties, RedisRefreshTokenService redisService) {
        this.jwtProperties = jwtProperties;
        this.redisService = redisService;
    }

    @Override
    public String createRefreshToken(String userId) {

        String token = UUID.randomUUID().toString();
        log.info(
                "Creating refresh token for userId={}",
                userId
        );
        long expirySeconds = TimeUnit.MILLISECONDS.toSeconds(jwtProperties.refreshExpiration());

        redisService.save(token, userId, expirySeconds);

        return token;

    }

    @Override
    public String validateRefreshToken(String token) {

        String userId = redisService.getUserIdByToken(token);

        if (userId == null) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        log.info("Refresh token validated");

        return userId;
    }

    @Override
    public void revokeRefreshToken(String token) {
        log.info("Revoking refresh token");
        redisService.delete(token);
    }




}
