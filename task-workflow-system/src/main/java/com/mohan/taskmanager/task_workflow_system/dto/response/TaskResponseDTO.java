package com.mohan.taskmanager.task_workflow_system.dto.response;

import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Task response object")
public record TaskResponseDTO(

        @Schema(description = "Task ID")
        UUID taskId,

        @Schema(description = "Task title")
        String title,

        @Schema(description = "Task description")
        String description,

        Priority priority,
        TaskStatus taskStatus,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime dueDate,

        String assignedUserName
) {}