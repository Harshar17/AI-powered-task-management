package com.ai.taskmanagement.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.ai.taskmanagement.exception.EmailAlreadyExistsException;
import com.ai.taskmanagement.exception.InvalidCredentialsException;
import com.ai.taskmanagement.exception.TaskNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFound(
            TaskNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(
            InvalidCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Task ID must be a number");
    }
}