package com.mohan.taskmanager.task_workflow_system.exception;

import com.mohan.taskmanager.task_workflow_system.dto.response.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //handling the error for task not found.
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskException(TaskNotFoundException ex){
        log.warn(
                "Task not found: {}",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        404,
                        ex.getMessage(),
                        LocalDateTime.now(),
                        null
                ));
    }

    //handling the error for user not found.
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserNotFoundException ex){
        log.warn(
                "User not found: {}",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        404,
                        ex.getMessage(),
                        LocalDateTime.now(),
                        null
                ));
    }

    //fall back
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        if (ex instanceof org.springframework.security.authorization.AuthorizationDeniedException) {
            log.warn(
                    "Access denied to protected resource"
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(
                            403,
                            "Access Denied",
                            LocalDateTime.now(),
                            null
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        500,
                        "Internal Server Error",
                        LocalDateTime.now(),
                        null
                ));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(HttpMessageNotReadableException ex){
        log.warn(
                "Validation failed: {}",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        400,
                        "Invalid request body",
                        LocalDateTime.now(),
                        null
                ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(NoResourceFoundException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        400,
                        "Invalid URL",
                        LocalDateTime.now(),
                        null
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()) );

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<APIResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        log.warn(
                "Invalid value '{}' supplied for parameter '{}'",
                ex.getValue(),
                ex.getName()
        );

        String message = "Invalid value '" + ex.getValue() + "' for parameter '"
                + ex.getName() + "'. Expected type: " + ex.getRequiredType().getSimpleName();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new APIResponse<>(
                        400,
                        message,
                        null
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn(
                "Illegal argument: {}",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        400,
                        ex.getMessage(),
                        LocalDateTime.now(),
                        null
                ));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<APIResponse<?>> handleUserExists(UserAlreadyExistsException ex) {
        log.warn(
                "User registration failed: {}",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new APIResponse<>(
                        409,
                        ex.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse<?>> handleDuplicateKey(DataIntegrityViolationException ex) {
        log.warn(
                "Duplicate data violation detected: {}",
                ex.getMostSpecificCause().getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new APIResponse<>(
                        409,
                        "Duplicate entry detected (email must be unique)",
                        null
                ));
    }
}
