package com.mohan.taskmanager.task_workflow_system.dto.request;

import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Schema(description = "Request payload for creating a task")
public class TaskRequestDTO {

    @Schema(description = "Title of task", example = "Fix login bug")
    private String title;

    @Schema(description = "Detailed description", example = "Fix JWT authentication issue")
    private String description;

    @Schema(description = "Task priority")
    private Priority priority;

    @Schema(description = "Due date of task")
    private LocalDateTime dueDate;
}