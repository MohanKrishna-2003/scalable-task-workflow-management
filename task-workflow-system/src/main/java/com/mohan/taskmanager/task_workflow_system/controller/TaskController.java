package com.mohan.taskmanager.task_workflow_system.controller;


import com.mohan.taskmanager.task_workflow_system.dto.request.AssignTaskDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.TaskRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.UpdateStatusDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.APIResponse;
import com.mohan.taskmanager.task_workflow_system.dto.response.TaskResponseDTO;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(
        name = "Task Management",
        description = "APIs for creating, assigning, updating, retrieving and deleting tasks"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }



    @Operation(
            summary = "Create Task",
            description = "Creates a new task. Only ADMIN users can create tasks."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<APIResponse<TaskResponseDTO>> createTask(@Valid @RequestBody TaskRequestDTO dto) {
        log.info(
                "Received request to create task title={}",
                dto.getTitle()
        );
        TaskResponseDTO responseDTO = taskService.createTask(dto);
        log.info(
                "Task created successfully taskId={}",
                responseDTO.taskId()
        );
        return ResponseEntity.status(201).body(new APIResponse<>(201, "Task created successfully", responseDTO));
    }

    @Operation(
            summary = "Get Tasks",
            description = """
                Retrieves tasks with optional filtering, sorting and pagination.
                
                Examples:
                - /tasks
                - /tasks?status=TODO
                - /tasks?page=0&size=5
                - /tasks?sort=createdAt,desc
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid filter or pagination values"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<APIResponse<Page<TaskResponseDTO>>> getTasks(
            @Parameter(
                    description = "Filter tasks by status",
                    example = "TODO"
            )
            @RequestParam(name = "status", required = false)
            TaskStatus taskStatus,

            @Parameter(
                    description = "Page number",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(
                    description = "Page size",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            int size,

            @Parameter(
                    description = "Sorting format: field,direction",
                    example = "createdAt,desc"
            )
            @RequestParam(defaultValue = "createdAt,desc")
            String sort
    ) {

        log.info(
                "Fetching tasks status={} page={} size={} sort={}",
                taskStatus,
                page,
                size,
                sort
        );
        Page<TaskResponseDTO> tasks = taskService.getTasks(taskStatus, page, size, sort);
        String message = tasks.isEmpty() ? "No tasks found for given filters" : "Tasks fetched successfully";
        log.info(
                "Fetched {} tasks",
                tasks.getNumberOfElements()
        );

        return ResponseEntity.ok(new APIResponse<>(200, message, tasks));
    }


    @Operation(
            summary = "Assign Task",
            description = "Assigns a task to a user. Only ADMIN users can perform this operation."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Task or user not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{taskId}/assignee")
    public ResponseEntity<APIResponse<Void>> assignTask(
            @Parameter(
                    description = "Task ID",
                    example = "d290f1ee-6c54-4b01-90e6-d701748f0851"
            )
            @PathVariable UUID taskId,
            @RequestBody AssignTaskDTO userId) {
        log.info(
                "Received request to assign task={} to user={}",
                taskId,
                userId.getUserId()
        );
        taskService.assignTask(taskId, userId);
        log.info(
                "Task {} assigned successfully",
                taskId
        );
        return ResponseEntity.ok(new APIResponse<>(200, "Task assigned successfully", null));
    }

    @Operation(
            summary = "Update Task Status",
            description = """
                Updates task status.
               
                ADMIN:
                - Can move task to any status.
                
                USER:
                - Can only update their own tasks.
                - Can move TODO → IN_PROGRESS only.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<APIResponse<Void>> updateStatus(
            @Parameter(
                    description = "Task ID",
                    example = "d290f1ee-6c54-4b01-90e6-d701748f0851"
            )
            @PathVariable
            UUID taskId,

            @Valid @RequestBody UpdateStatusDTO status
    ){

        log.info(
                "Updating task={} status to {}",
                taskId,
                status.getTaskStatus()
        );
        taskService.updateStatus(taskId, status);
        log.info(
                "Task status updated taskId={}",
                taskId
        );
        return ResponseEntity.ok(new APIResponse<>(200, "Status updated successfully", null));
    }

    @Operation(
            summary = "Delete Task",
            description = """
                Soft deletes a task.
                
                The task is not removed from the database.
                It is marked as archived.
                Only ADMIN users can perform this action.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<APIResponse<Void>> deleteTask(
            @Parameter(
                    description = "Task ID",
                    example = "d290f1ee-6c54-4b01-90e6-d701748f0851"
            )
            @PathVariable
            UUID taskId
    ) {
        log.warn(
                "Received request to delete task={}",
                taskId
        );
        taskService.deleteTask(taskId);
        log.info(
                "Task deleted successfully taskId={}",
                taskId
        );
        return ResponseEntity.ok(new APIResponse<>(200, "Tasks deleted successfully", null));
    }
}