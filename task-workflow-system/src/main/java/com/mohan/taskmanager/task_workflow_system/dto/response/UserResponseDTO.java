package com.mohan.taskmanager.task_workflow_system.dto.response;

public record UserResponseDTO(
        String userId,
        String name,
        String email,
        String role
) {}
