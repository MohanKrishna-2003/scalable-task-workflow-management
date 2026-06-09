package com.mohan.taskmanager.task_workflow_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Login Request")
public class LoginRequestDTO {

    @Schema(
            description = "User email address",
            example = "luffy@email.com"
    )
    private String email;

    @Schema(
            description = "User password",
            example = "Password@123"
    )
    private String password;
}