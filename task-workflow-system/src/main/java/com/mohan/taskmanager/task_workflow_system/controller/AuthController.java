package com.mohan.taskmanager.task_workflow_system.controller;

import com.mohan.taskmanager.task_workflow_system.dto.request.LoginRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.LogoutRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.request.RefreshTokenRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.LoginResponseDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.RefreshTokenResponseDTO;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(
        name = "Authentication",
        description = "Authentication APIs for login, refresh token, and logout operations"
)

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @Operation(
            summary = "User Login",
            description = "Authenticates a user using email and password and returns an access token and refresh token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto){
        log.info(
                "Login attempt for email={}",
                dto.getEmail()
        );
        LoginResponseDTO response = authService.login(dto.getEmail(), dto.getPassword());
        log.info(
                "Login successful for email={}",
                dto.getEmail()
        );
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Refresh Access Token",
            description = "Generates a new access token and refresh token using a valid refresh token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "401", description = "Refresh token expired or revoked"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO dto) {
        log.info("Refresh token request received");
        RefreshTokenResponseDTO response = authService.refreshToken(dto.getRefreshToken());
        log.info("Refresh token generated successfully");
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Logout User",
            description = "Revokes the refresh token and logs out the user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDTO dto) {
        log.info("Logout request received");
        authService.logout(dto.getRefreshToken());
        log.info("User logged out successfully");
        return ResponseEntity.ok("Logged out successfully");
    }
}
