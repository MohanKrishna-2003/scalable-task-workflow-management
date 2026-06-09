package com.mohan.taskmanager.task_workflow_system.service.interfaces;

import com.mohan.taskmanager.task_workflow_system.dto.response.LoginResponseDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.RefreshTokenResponseDTO;

public interface AuthService {
    LoginResponseDTO login(String email, String password);

    RefreshTokenResponseDTO refreshToken(String refreshToken);

    void logout(String refreshToken);

}
