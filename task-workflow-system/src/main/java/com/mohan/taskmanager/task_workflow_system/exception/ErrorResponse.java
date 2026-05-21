package com.mohan.taskmanager.task_workflow_system.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timeStamp,
        Map<String, String> errors
) {}
