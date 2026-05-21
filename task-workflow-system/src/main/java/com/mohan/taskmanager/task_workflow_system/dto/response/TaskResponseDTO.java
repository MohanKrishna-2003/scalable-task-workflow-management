package com.mohan.taskmanager.task_workflow_system.dto.response;

import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        java.util.UUID taskId,
        String title,
        String description,
        Priority priority,
        TaskStatus taskStatus,
        LocalDateTime dueDate,
        String assignedUserName // or userId
){}
