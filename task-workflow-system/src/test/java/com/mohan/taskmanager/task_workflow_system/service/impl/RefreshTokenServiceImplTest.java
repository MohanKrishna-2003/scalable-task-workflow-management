package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.config.security.JwtProperties;
import com.mohan.taskmanager.task_workflow_system.service.redis.RedisRefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RedisRefreshTokenService redisService;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Test
    void shouldCreateRefreshToken() {

        String userId = UUID.randomUUID().toString();

        when(jwtProperties.refreshExpiration())
                .thenReturn(86400000L);

        String token = refreshTokenService.createRefreshToken(userId);

        assertNotNull(token);
        assertEquals(userId, token);
        assertNotNull(token);

        verify(redisService, times(1))
                .save(eq(token), eq(userId), anyLong());
    }

    @Test
    void shouldValidateRefreshToken() {

        when(redisService.getUserIdByToken("token"))
                .thenReturn("user123");

        String result =
                refreshTokenService.validateRefreshToken("token");

        assertEquals("token", result);
        assertEquals("user123", result);
    }

    @Test
    void shouldThrowExceptionWhenTokenInvalid() {

        when(redisService.getUserIdByToken("token"))
                .thenReturn(null);

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> refreshTokenService.validateRefreshToken("token")
                );

        assertEquals("Invalid or expired refresh token", exception.getMessage());
    }

    @Test
    void shouldRevokeRefreshToken() {

        refreshTokenService.revokeRefreshToken("token");

        verify(redisService, times(1)).delete("token");
    }
}