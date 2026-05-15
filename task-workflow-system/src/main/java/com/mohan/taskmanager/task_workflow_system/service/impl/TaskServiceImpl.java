package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.exception.TaskNotFoundException;
import com.mohan.taskmanager.task_workflow_system.exception.UserNotFoundException;
import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.TaskService;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {
    private Map<String, Task> taskStore = new HashMap<>();

    private final UserService userService;

    public TaskServiceImpl(UserService userService) {
        this.userService = userService;
    }


    private String generateTaskId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Task createTask(String title, String description, Priority priority){
        String taskId = generateTaskId();
        Task task = new Task(taskId, title, description, priority);
        taskStore.put(taskId, task);
        return task;
    }

    @Override
    public void assignTask(String taskId, String userId){
        Task task = taskStore.get(taskId);
        if(taskId == null){
            throw new TaskNotFoundException("Task not found with id " + taskId);
        }
        User user = userService.getUserById(userId);
        if(user == null){
            throw new UserNotFoundException("User not found with id " + userId);
        }
        task.assignUser(user);
    }

    @Override
    public void updateStatus(String taskId, TaskStatus taskStatus){
        Task task = taskStore.get(taskId);
        if(task == null){
            throw new TaskNotFoundException("Task not found with id " + taskId);
        }
        task.updateStatus(taskStatus);
    }

    @Override
    public void updatePriority(String taskId, Priority priority){
        Task task = taskStore.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task not found with id " + taskId);
        }
        task.updatePriority(priority);
    }

    @Override
    public List<Task> getTasksByUser(String userId){
        List<Task> tasks = new ArrayList<>();
        for(Task task: taskStore.values()){
            if(task.getAssignedUser()!=null && task.getAssignedUser().getUserId().equals(userId)){
                tasks.add(task);
            }
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus taskStatus){
        List<Task> tasks = new ArrayList<>();
        for(Task task: taskStore.values()){
            if(task.getStatus().equals(taskStatus)){
                tasks.add(task);
            }
        }
        return tasks;
    }

    @Override
    public void deleteTask(String taskId){
        if(!taskStore.containsKey(taskId)){
            throw new TaskNotFoundException("Task not found with id " + taskId);
        }
        taskStore.remove(taskId);
    }

    @Override
    public List<Task> getAllTasks(){
        return new ArrayList<>(taskStore.values());
    }

}
