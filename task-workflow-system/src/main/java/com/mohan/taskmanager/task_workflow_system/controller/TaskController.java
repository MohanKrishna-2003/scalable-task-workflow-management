package com.mohan.taskmanager.task_workflow_system.controller;


import com.mohan.taskmanager.task_workflow_system.dto.request.AssignTaskDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.TaskRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.UpdateStatusDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.APIResponse;
import com.mohan.taskmanager.task_workflow_system.dto.response.TaskResponseDTO;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<TaskResponseDTO>> createTask(@Valid @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO responseDTO = taskService.createTask(dto);
        return ResponseEntity.status(201).body(new APIResponse<>(201, "Task created successfully", responseDTO));
    }


    @GetMapping
    public ResponseEntity<APIResponse<List<TaskResponseDTO>>> getTasks(
            @RequestParam(required = false) String userId,
            @RequestParam(name = "status", required = false) TaskStatus taskStatus
    ) {

        List<TaskResponseDTO> tasks = taskService.getTasks(userId, taskStatus);

        String message = tasks.isEmpty() ? "No tasks found for given filters" : "Tasks fetched successfully";

        return ResponseEntity.ok(new APIResponse<>(200, message, tasks));
    }


    @PatchMapping("/{taskId}/assignee")
    public ResponseEntity<APIResponse<Void>> assignTask(@PathVariable UUID taskId,
                                                        @RequestBody AssignTaskDTO userId) {
        taskService.assignTask(taskId, userId);
        return ResponseEntity.ok(new APIResponse<>(200, "Task assigned successfully", null));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<APIResponse<Void>> updateStatus(@PathVariable UUID taskId, @Valid @RequestBody UpdateStatusDTO status) {
        taskService.updateStatus(taskId, status);
        return ResponseEntity.ok(new APIResponse<>(200, "Status updated successfully", null));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<APIResponse<Void>> deleteTask(@PathVariable UUID taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(new APIResponse<>(200, "Tasks deleted successfully", null));
    }
}