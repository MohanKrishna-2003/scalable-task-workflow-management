package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.dto.request.AssignTaskDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.TaskRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.UpdateStatusDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.TaskResponseDTO;
import com.mohan.taskmanager.task_workflow_system.exception.TaskNotFoundException;
import com.mohan.taskmanager.task_workflow_system.exception.UserNotFoundException;
import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.mapper.TaskMapper;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.repository.TaskRepository;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.TaskService;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {
//    private Map<String, Task> taskStore = new HashMap<>();

    private TaskRepository taskRepository;
    private final UserService userService;

    public TaskServiceImpl(UserService userService, TaskRepository taskRepository) {
        this.userService = userService;
        this.taskRepository = taskRepository;
    }


    @Override
    public TaskResponseDTO createTask(TaskRequestDTO dto){
        Task task = TaskMapper.toEntity(dto);
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());
        task.setArchived(false);
        task.setCreatedBy("system-user"); // later we can replace it with jwt.

        Task saved = taskRepository.save(task);

        return TaskMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void assignTask(UUID taskId, AssignTaskDTO userId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + taskId));
        User user = userService.getUserById(userId.getUserId());
        task.assignUser(user);
    }

    @Override
    @Transactional
    public void updateStatus(UUID taskId, UpdateStatusDTO statusDTO){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + taskId));

        task.updateNewStatus(statusDTO.getTaskStatus());
    }

    @Override
    public void updatePriority(UUID taskId, Priority priority){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + taskId));

        task.updatePriority(priority);
    }

    private Sort parseSort(String sort) {

        if (sort == null || sort.isBlank()) {
            return Sort.by("createdAt").descending();
        }

        String[] parts = sort.split(",");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid sort format. Use field,asc or field,desc");
        }

        String field = parts[0].trim();
        String direction = parts[1].trim().toLowerCase();

        if (field.isEmpty()) {
            throw new IllegalArgumentException("Sort field cannot be empty");
        }

        return switch (direction) {
            case "asc" -> Sort.by(field).ascending();
            case "desc" -> Sort.by(field).descending();
            default -> throw new IllegalArgumentException(
                    "Invalid sort direction: " + direction + ". Use asc or desc"
            );
        };
    }

    @Override
    public Page<TaskResponseDTO> getTasks(String userId, TaskStatus taskStatus, int page, int size, String sort){

        Sort sorting = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<Task> tasks;
        if(userId != null && taskStatus != null){
            tasks = taskRepository.findByAssignedUser_UserIdAndStatus(userId, taskStatus, pageable);
        } else if(userId != null){
            tasks = taskRepository.findByAssignedUser_UserId(userId, pageable);
        } else if(taskStatus != null){
            tasks = taskRepository.findByStatus(taskStatus, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }
        return tasks.map(TaskMapper::toDTO);
    }

    @Override
    public void deleteTask(UUID taskId){
        if(!taskRepository.existsById(taskId)){
            throw new TaskNotFoundException("Task not found with id " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

}
