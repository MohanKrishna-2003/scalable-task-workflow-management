package com.mohan.taskmanager.task_workflow_system.controller;


import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String description = request.get("description");
        Priority priority = Priority.valueOf(request.get("priority"));
        Task task = taskService.createTask(title, description, priority);
        return ResponseEntity.status(201).body(task);
    }


    @PostMapping("/{taskId}/assign")
    public ResponseEntity<String> assignTask(@PathVariable String taskId,
                           @RequestParam String userId) {
        taskService.assignTask(taskId, userId);
        return ResponseEntity.ok("Task assigned successfully");
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<String> updateStatus(@PathVariable String taskId,
                             @RequestParam TaskStatus status) {
        taskService.updateStatus(taskId, status);
        return ResponseEntity.ok("Status updated successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable String userId) {
        List<Task> tasks = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status")
    public List<Task> getTasksByStatus(@RequestParam TaskStatus status) {
        return taskService.getTasksByStatus(status);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }


}