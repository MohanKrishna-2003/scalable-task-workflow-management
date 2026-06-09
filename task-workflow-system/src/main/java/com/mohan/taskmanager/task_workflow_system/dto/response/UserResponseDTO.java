package com.mohan.taskmanager.task_workflow_system.dto.response;

import com.mohan.taskmanager.task_workflow_system.enums.Role;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for user details")
public record UserResponseDTO(

        @Schema(description = "Full name of user", example = "Monkey D. Luffy")
        String name,

        @Schema(description = "Email address", example = "luffy@email.com")
        String email,

        @Schema(description = "Role of the user", example = "USER")
        Role role
) {}
