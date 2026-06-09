package com.mohan.taskmanager.task_workflow_system.dto.request;

import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "Request to update task status")
public class UpdateStatusDTO {

    @NotNull
    @Schema(description = "New task status", example = "IN_PROGRESS")
    private TaskStatus taskStatus;
}
