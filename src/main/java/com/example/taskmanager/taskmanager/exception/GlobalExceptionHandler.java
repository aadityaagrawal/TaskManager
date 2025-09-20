package com.example.taskmanager.taskmanager.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex)
    {
        Map<String, Object> response = new HashMap<>();
        response.put("response_code", HttpStatus.BAD_REQUEST.value());
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.TaskDeletionException.class)
    public ResponseEntity<Map<String,Object>> handleTaskDeletionException(CustomExceptions.TaskDeletionException ex)
    {
        Map<String, Object> response = new HashMap<>();
        response.put("response_code", HttpStatus.BAD_REQUEST.value());
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String,Object>> handleNoSuchElementException(NoSuchElementException ex)
    {
        Map<String, Object> response = new HashMap<>();
        response.put("response_code", HttpStatus.NOT_FOUND.value());
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleIllegalArgumentException(IllegalArgumentException ex)
    {
        Map<String, Object> response = new HashMap<>();
        response.put("response_code", HttpStatus.BAD_REQUEST.value());
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentError (MethodArgumentNotValidException ex)
    {
        Map<String, Object> response = new HashMap<>();
        response.put("response_code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAllExceptions(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("response_code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}

