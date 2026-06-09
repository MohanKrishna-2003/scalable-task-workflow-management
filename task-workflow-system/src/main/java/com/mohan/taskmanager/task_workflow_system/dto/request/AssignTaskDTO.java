package com.mohan.taskmanager.task_workflow_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
@Schema(description = "Assign task to a user")
public class AssignTaskDTO {

    @Schema(description = "User ID to assign task", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID userId;
}