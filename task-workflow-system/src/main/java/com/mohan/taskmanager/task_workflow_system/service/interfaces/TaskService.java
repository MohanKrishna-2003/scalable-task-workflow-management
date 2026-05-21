package com.mohan.taskmanager.task_workflow_system.service.interfaces;

import com.mohan.taskmanager.task_workflow_system.dto.request.AssignTaskDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.TaskRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.UpdateStatusDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.TaskResponseDTO;
import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface TaskService {
    // so here this service gives what it can do. that's it
    TaskResponseDTO createTask(TaskRequestDTO dto);

    void assignTask(UUID taskId, AssignTaskDTO userId);

    void updateStatus(UUID taskId, UpdateStatusDTO statusDTO);

    void updatePriority(UUID taskId, Priority priority);

    Page<TaskResponseDTO> getTasks(String userId, TaskStatus taskStatus, int page, int size, String sort);

//    List<TaskResponseDTO> getAllTasks();

    void deleteTask(UUID taskId);
}
