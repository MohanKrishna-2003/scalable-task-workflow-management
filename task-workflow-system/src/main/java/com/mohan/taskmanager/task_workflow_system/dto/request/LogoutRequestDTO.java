package com.mohan.taskmanager.task_workflow_system.dto.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Request payload for logging out user")
public class LogoutRequestDTO {

    @Schema(
            description = "Refresh token to invalidate session",
            example = "a1b2c3d4-refresh-token"
    )
    private String refreshToken;
}