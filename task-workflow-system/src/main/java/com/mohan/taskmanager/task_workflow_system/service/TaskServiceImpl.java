package com.mohan.taskmanager.task_workflow_system.service;

import com.mohan.taskmanager.task_workflow_system.model.Priority;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.model.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.model.User;

import java.util.*;

public class TaskServiceImpl implements TaskService{
    private Map<String, Task> taskStore = new HashMap<>();
    private Map<String, User> userStore = new HashMap<>();

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
        User user = userStore.get(userId);
        if(taskId == null){
            throw new RuntimeException("Task not found");
        }
        if(userId == null){
            throw new RuntimeException("User not found");
        }
        task.assignUser(user);
    }

    @Override
    public void updateStatus(String taskId, TaskStatus taskStatus){
        Task task = taskStore.get(taskId);
        if(task == null){
            throw new RuntimeException("Task not found");
        }
        task.updateStatus(taskStatus);
    }

    @Override
    public void updatePriority(String taskId, Priority priority){
        Task task = taskStore.get(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found");
        }
        task.updatePriority(priority);
    }

    @Override
    public List<Task> getTaskByUser(String userId){
        List<Task> result = new ArrayList<>();
        for(Task task: taskStore.values()){
            if(task.getAssignedUser()!=null && task.getAssignedUser().getUserId().equals(userId)){
                result.add(task);
            }
        }
        return result;
    }

    @Override
    public List<Task> getTaskByStatus(TaskStatus taskStatus){
        List<Task> result = new ArrayList<>();
        for(Task task: taskStore.values()){
            if(task.getStatus().equals(taskStatus)){
                result.add(task);
            }
        }
        return result;
    }

    @Override
    public void deleteTask(String taskId){
        if(!taskStore.containsKey(taskId)){
            throw new RuntimeException("Task not found");
        }
        taskStore.remove(taskId);
    }

    // temporary method to get the userStore
    public Map<String, User> getUserStore() {
        return userStore;
    }

}
