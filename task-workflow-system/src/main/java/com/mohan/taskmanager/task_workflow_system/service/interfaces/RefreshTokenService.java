package com.mohan.taskmanager.task_workflow_system.service.interfaces;

public interface RefreshTokenService {

    String createRefreshToken(String userId);

    String validateRefreshToken(String token);

    void revokeRefreshToken(String token);
}
