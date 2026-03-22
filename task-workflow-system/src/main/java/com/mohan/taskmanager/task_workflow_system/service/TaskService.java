package com.mohan.taskmanager.task_workflow_system.service;

import com.mohan.taskmanager.task_workflow_system.model.Priority;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.model.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.model.User;

import java.util.List;

public interface TaskService {
    // so here this service gives what it can do. that's it
    Task createTask(String title, String description, Priority priority);

    User createUser(String userId, String name, String email);

    void assignTask(String taskId, String userId);

    void updateStatus(String taskId, TaskStatus taskStatus);

    void updatePriority(String taskId, Priority priority);

    List<Task> getTaskByUser(String userId);

    List<Task> getTaskByStatus(TaskStatus taskStatus);

    void deleteTask(String taskId);
}
