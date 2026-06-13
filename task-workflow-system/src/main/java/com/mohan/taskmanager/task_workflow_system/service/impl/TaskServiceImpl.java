package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.config.security.SecurityUtils;
import com.mohan.taskmanager.task_workflow_system.dto.request.AssignTaskDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.TaskRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.UpdateStatusDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.TaskResponseDTO;
import com.mohan.taskmanager.task_workflow_system.events.TaskAssignedEvent;
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
import com.mohan.taskmanager.task_workflow_system.specification.TaskSpecification;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    public TaskServiceImpl(UserService userService, TaskRepository taskRepository, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO dto){
        Task task = TaskMapper.toEntity(dto);
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());
        task.setArchived(false);
        task.setCreatedBy("system-user"); // later we can replace it with jwt.
        log.info(
                "Creating task title={}",
                dto.getTitle()
        );
        Task saved = taskRepository.save(task);
        log.info(
                "Task created taskId={}",
                saved.getTaskId()
        );

        return TaskMapper.toDTO(saved);
    }

    // see, we are using transactional here because:  @Transactional + Dirty Checking = automatic update in dB.
//    Whenever you do:
//    FETCH ENTITY → MODIFY ENTITY
//    Use: @Transactional
    @Override
    @Transactional
    public void assignTask(UUID taskId, AssignTaskDTO userId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + taskId));

        User user = userService.getUserById(userId.getUserId());
        log.info(
                "Assigning task {} to user {}",
                taskId,
                user.getUserId()
        );
        task.assignUser(user);

        log.info(
                "Publishing TaskAssignedEvent taskId={}",
                taskId
        );
        // publish an event.
        eventPublisher.publishEvent(
                new TaskAssignedEvent(
                        task.getTaskId(),
                        user.getName(),
                        user.getEmail(),
                        task.getTitle(),
                        task.getDescription()
                ));

    }


    @Override
    @Transactional
    public void updateStatus(UUID taskId, UpdateStatusDTO statusDTO){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id " + taskId));


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities()
                        .stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        String email = authentication.getName();

        boolean isOwner = task.getAssignedUser() != null && task.getAssignedUser().getEmail().equals(email);

        TaskStatus current = task.getStatus();
        TaskStatus requested = statusDTO.getTaskStatus();
        log.info(
                "Updating task status taskId={} from={} to={}",
                taskId,
                current,
                requested
        );
        // now, USER can only move TODO -> IN_PROGRESS
        if(!isAdmin){
            if(!isOwner){
                log.warn(
                        "Unauthorized task update attempt by {}",
                        email
                );
                throw new AccessDeniedException("You can update only your tasks");
            }

            if(!(current == TaskStatus.TODO && requested == TaskStatus.IN_PROGRESS)){
                throw new AccessDeniedException("User can only move TODO to IN_PROGRESS");
            }
        }

        // or else admin can do everything.
        task.updateNewStatus(statusDTO.getTaskStatus());
    }

    @Override
    @Transactional
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
    public Page<TaskResponseDTO> getTasks(TaskStatus taskStatus, int page, int size, String sort){

        String email = SecurityUtils.getCurrentUserEmail();

        Sort sorting = parseSort(sort);

        Pageable pageable = PageRequest.of(page, size, sorting);
        Specification<Task> specification = TaskSpecification.build(email, taskStatus);

        // new way - using dto projection
//        return taskRepository.findAllTasksDTO(specification, pageable);

//         preferable way because we should not use Page with DTO projection.
        Page<Task> tasks = taskRepository.findAll(specification, pageable);
        return tasks.map(TaskMapper::toDTO);
    }


    @Override
    @Transactional
    public void deleteTask(UUID taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        log.warn(
                "Soft deleting task {}",
                taskId
        );
        // so here it is hard delete, we are deleting it permanently,
//        taskRepository.deleteById(taskId);

        // here we will use soft delete instead, by setting archived = true.
        task.setArchived(true);
    }

}
