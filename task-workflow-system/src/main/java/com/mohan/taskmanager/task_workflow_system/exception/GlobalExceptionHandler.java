package com.mohan.taskmanager.task_workflow_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //handling the error for task not found.
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskException(TaskNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    //handling the error for user not found.
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserException(UserNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    //fall back
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handlePriority(IllegalArgumentException ex){

        if(ex.getMessage().contains("No enum constant")){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Property. Values should be of {LOW, MEDIUM, HIGH}");
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

}
