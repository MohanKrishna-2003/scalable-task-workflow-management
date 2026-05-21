package com.mohan.taskmanager.task_workflow_system.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;


public record APIResponse<T>(int status, String message, T data, LocalDateTime timestamp) {
    public APIResponse(int status, String message, T data){
        this(status, message, data, LocalDateTime.now());
    }
}
