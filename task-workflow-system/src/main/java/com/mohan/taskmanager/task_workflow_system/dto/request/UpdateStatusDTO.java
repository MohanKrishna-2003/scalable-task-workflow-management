package com.mohan.taskmanager.task_workflow_system.dto.request;

import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateStatusDTO {

    @NotNull
    private TaskStatus taskStatus;
}
