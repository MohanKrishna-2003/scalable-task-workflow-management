package com.mohan.taskmanager.task_workflow_system.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "Health Check", description = "API health monitoring endpoint")
public class HomeController {

    @Operation(
            summary = "Health check API",
            description = "Returns service status to confirm API is running"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Service is running successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class)
            )
    )
    @GetMapping("/")
    public Map<String, Object> home() {

        log.info("Health check endpoint called");

        return Map.of(
                "status", "UP",
                "service", "Task Workflow API",
                "timestamp", LocalDateTime.now()
        );
    }
}