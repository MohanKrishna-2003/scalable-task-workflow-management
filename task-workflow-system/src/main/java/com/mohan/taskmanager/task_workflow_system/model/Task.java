package com.mohan.taskmanager.task_workflow_system.model;


import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.exception.UserNotFoundException;

import java.time.LocalDateTime;

public class Task {
    private String taskId;
    private String title;
    private String description;
    private Priority priority;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private User assignedUser;

    public Task(String taskId, String title, String description, Priority priority){
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = TaskStatus.TODO;
        this.createdAt = LocalDateTime.now();
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    // Behavior Methods (Important for LLD)
    public void assignUser(User user){
        if(user == null){
            throw new UserNotFoundException("User cannot be null");
        }
        this.assignedUser = user;
    }

    public void updateStatus(TaskStatus newStatus){
        if(newStatus == null){
            throw new IllegalArgumentException("Status cannot be null");
        }
        if(status == newStatus) return;
        if(status == TaskStatus.TODO && newStatus == TaskStatus.IN_PROGRESS){
            this.status = newStatus;
        } else if (this.status == TaskStatus.IN_PROGRESS && newStatus == TaskStatus.COMPLETED) {
            this.status = newStatus;
        } else {
            throw new IllegalStateException(
                    "Invalid status transition from " + this.status + " to " + newStatus
            );
        }




    }

    public void updatePriority(Priority priority){
        if(priority == null){
            throw new IllegalStateException("Priority cannot be null");
        }
        this.priority = priority;
    }
}
