package com.mohan.taskmanager.task_workflow_system.mapper;

import com.mohan.taskmanager.task_workflow_system.dto.request.TaskRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.TaskResponseDTO;
import com.mohan.taskmanager.task_workflow_system.model.Task;

public class TaskMapper {

    public static Task toEntity(TaskRequestDTO dto){
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        return task;
    }

    public static TaskResponseDTO toDTO(Task task){
        String assignedUserName = task.getAssignedUser() != null ? task.getAssignedUser().getName() : null;
        return new TaskResponseDTO(
                task.getTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                assignedUserName
        );
    }

}
