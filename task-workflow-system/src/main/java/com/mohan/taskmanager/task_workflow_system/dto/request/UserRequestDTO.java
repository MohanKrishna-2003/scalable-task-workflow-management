package com.mohan.taskmanager.task_workflow_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Schema(description = "Request payload for creating a new user")
public class UserRequestDTO {

    @Schema(description = "User full name", example = "Monkey D. Luffy")
    @NotBlank(message = "Username is required")
    private String name;

    @Schema(description = "User email address", example = "luffy@email.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is incorrect")
    private String email;

    @Schema(
            description = "Password must be strong",
            example = "StrongP@ss123"
    )
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "Must contain uppercase, lowercase, number"
    )
    private String password;
}
