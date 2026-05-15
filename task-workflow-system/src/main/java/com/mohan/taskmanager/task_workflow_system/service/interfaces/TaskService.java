package com.mohan.taskmanager.task_workflow_system.service.interfaces;

import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    // so here this service gives what it can do. that's it
    Task createTask(String title, String description, Priority priority);

    void assignTask(String taskId, String userId);

    void updateStatus(String taskId, TaskStatus taskStatus);

    void updatePriority(String taskId, Priority priority);

    List<Task> getTasksByUser(String userId);

    List<Task> getTasksByStatus(TaskStatus taskStatus);

    List<Task> getAllTasks();

    void deleteTask(String taskId);
}
