package com.mohan.taskmanager.task_workflow_system.controller;

import com.mohan.taskmanager.task_workflow_system.dto.request.UserRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.APIResponse;
import com.mohan.taskmanager.task_workflow_system.dto.response.UserResponseDTO;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Users", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(
            summary = "Get all users",
            description = "Fetches list of all registered users in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<APIResponse<List<UserResponseDTO>>> getAllUsers(){

        log.info("GET /users request received");

        List<UserResponseDTO> users = userService.getAllUsers();

        log.info("Users fetched successfully count={}", users.size());

        return ResponseEntity.ok(
                new APIResponse<>(200, "Users fetched successfully", users)
        );
    }

    @Operation(
            summary = "Create a new user",
            description = "Registers a new user with name, email and password"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<APIResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody UserRequestDTO dto
    ) {

        log.info("POST /users request received email={}", dto.getEmail());

        UserResponseDTO user = userService.createUser(dto);

        log.info("User created successfully email={}", user.email());

        return ResponseEntity.status(201).body(
                new APIResponse<>(201, "User created successfully", user)
        );
    }
}