package com.mohan.taskmanager.task_workflow_system.service;

import com.mohan.taskmanager.task_workflow_system.model.Priority;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.model.TaskStatus;

import java.util.List;

public interface TaskService {
    // so here this service gives what jobs or services it can do. that's it
    Task createTask(String title, String description, Priority priority);

    void assignTask(String taskId, String userId);

    void updateStatus(String taskId, TaskStatus taskStatus);

    void updatePriority(String taskId, Priority priority);

    List<Task> getTaskByUser(String userId);

    List<Task> getTaskByStatus(TaskStatus taskStatus);

    void deleteTask(String taskId);
}
