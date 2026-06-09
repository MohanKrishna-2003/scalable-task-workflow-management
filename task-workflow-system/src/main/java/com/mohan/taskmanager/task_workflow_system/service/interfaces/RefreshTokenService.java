package com.mohan.taskmanager.task_workflow_system.service.interfaces;

import com.mohan.taskmanager.task_workflow_system.model.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String userId);

    RefreshToken validateRefreshToken(String token);

    void revokeRefreshToken(String token);
}
