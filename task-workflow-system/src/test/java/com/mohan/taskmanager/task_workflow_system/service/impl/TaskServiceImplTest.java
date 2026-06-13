package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.config.security.SecurityUtils;
import com.mohan.taskmanager.task_workflow_system.dto.request.AssignTaskDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.TaskRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.UpdateStatusDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.TaskResponseDTO;
import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.events.TaskAssignedEvent;
import com.mohan.taskmanager.task_workflow_system.exception.TaskNotFoundException;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.repository.TaskRepository;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private User user;
    private UUID taskId;
    private UUID userId;

    @BeforeEach
    void setUp() {

        taskId = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = new User();
        user.setUserId(userId);
        user.setName("Luffy");
        user.setEmail("luffy@test.com");

        task = new Task();
        task.setTaskId(taskId);
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setStatus(TaskStatus.TODO);
        task.setAssignedUser(user);
    }

    @Test
    void createTask_ShouldCreateTaskSuccessfully() {

        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Task");
        dto.setDescription("Desc");
        dto.setPriority(Priority.HIGH);

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDTO response = taskService.createTask(dto);

        assertNotNull(response);

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void assignTask_ShouldAssignUserSuccessfully() {

        AssignTaskDTO dto = new AssignTaskDTO();
        dto.setUserId(userId);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(userService.getUserById(userId))
                .thenReturn(user);

        taskService.assignTask(taskId, dto);

        verify(eventPublisher).publishEvent(any(TaskAssignedEvent.class));
    }

    @Test
    void assignTask_TaskNotFound_ShouldThrowException() {

        AssignTaskDTO dto = new AssignTaskDTO();
        dto.setUserId(userId);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.assignTask(taskId, dto)
        );
    }

    @Test
    void updateStatus_AdminCanUpdateAnyStatus() {

        UpdateStatusDTO dto = mock(UpdateStatusDTO.class);

        when(dto.getTaskStatus()).thenReturn(TaskStatus.COMPLETED);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "admin@test.com",
                        null,
                        List.of(() -> "ROLE_ADMIN")
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        taskService.updateStatus(taskId, dto);

        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    void updateStatus_UserCanMoveTodoToInProgress() {

        UpdateStatusDTO dto = mock(UpdateStatusDTO.class);

        when(dto.getTaskStatus()).thenReturn(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "luffy@test.com",
                        null,
                        List.of(() -> "ROLE_USER")
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        taskService.updateStatus(taskId, dto);

        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void updateStatus_NotOwner_ShouldThrowAccessDenied() {

        UpdateStatusDTO dto = mock(UpdateStatusDTO.class);

        when(dto.getTaskStatus()).thenReturn(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "zoro@test.com",
                        null,
                        List.of(() -> "ROLE_USER")
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(
                AccessDeniedException.class,
                () -> taskService.updateStatus(taskId, dto)
        );
    }

    @Test
    void updateStatus_InvalidTransition_ShouldThrowAccessDenied() {

        UpdateStatusDTO dto = mock(UpdateStatusDTO.class);

        when(dto.getTaskStatus()).thenReturn(TaskStatus.COMPLETED);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "luffy@test.com",
                        null,
                        List.of(() -> "ROLE_USER")
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(
                AccessDeniedException.class,
                () -> taskService.updateStatus(taskId, dto)
        );
    }

    @Test
    void updatePriority_ShouldUpdatePriority() {

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        taskService.updatePriority(taskId, Priority.HIGH);

        assertEquals(Priority.HIGH, task.getPriority());
    }

    @Test
    void updatePriority_TaskNotFound_ShouldThrowException() {

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updatePriority(taskId, Priority.HIGH)
        );
    }

    @Test
    void getTasks_ShouldReturnPage() {

        Page<Task> page =
                new PageImpl<>(List.of(task));

        when(taskRepository.findAll((Specification<Task>) any(), any(Pageable.class)))
                .thenReturn(page);

        try (MockedStatic<SecurityUtils> mocked =
                     mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("luffy@test.com");

            Page<TaskResponseDTO> result =
                    taskService.getTasks(
                            TaskStatus.TODO,
                            0,
                            10,
                            "createdAt,desc"
                    );

            assertEquals(1, result.getContent().size());
        }
    }

    @Test
    void deleteTask_ShouldSoftDeleteTask() {

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        assertTrue(task.isArchived());
    }

    @Test
    void deleteTask_TaskNotFound_ShouldThrowException() {

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(taskId)
        );
    }
}