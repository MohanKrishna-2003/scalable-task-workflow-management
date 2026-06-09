package com.mohan.taskmanager.task_workflow_system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Login Response")
public class LoginResponseDTO {

    @Schema(
            description = "JWT access token"
    )
    private String accessToken;

    @Schema(
            description = "Refresh token"
    )
    private String refreshToken;
}
