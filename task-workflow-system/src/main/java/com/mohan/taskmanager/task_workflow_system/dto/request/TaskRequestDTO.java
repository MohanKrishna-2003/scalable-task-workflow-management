package com.mohan.taskmanager.task_workflow_system.dto.request;

import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestDTO {
    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private LocalDateTime dueDate;
}
