package com.example.taskmanager.taskmanager.exception;

import com.example.taskmanager.taskmanager.model.ErrorResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.BAD_REQUEST.value()).message("Database constraint violated: " + ex.getMostSpecificCause().getMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.TaskDeletionException.class)
    public ResponseEntity<ErrorResponse> handleTaskDeletionException(CustomExceptions.TaskDeletionException ex) {
        ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.NOT_FOUND.value()).message(ex.getMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.BAD_REQUEST.value()).message("Validation failed for one or more fields").errors(fieldErrors).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // log for debugging

        ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("An unexpected error occurred").build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidEx) {
            String invalidValue = invalidEx.getValue().toString();
            Class<?> targetType = invalidEx.getTargetType();

            String allowedValues = "";
            if (targetType.isEnum()) {
                Object[] enumValues = targetType.getEnumConstants();
                allowedValues = String.join(", ", java.util.Arrays.stream(enumValues).map(Object::toString).toArray(String[]::new));
            }

            String message = "Invalid value '" + invalidValue + "' for " + targetType.getSimpleName() + ". Allowed values are: " + allowedValues;

            ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.BAD_REQUEST.value()).message(message).build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ErrorResponse response = ErrorResponse.builder().timestamp(LocalDateTime.now()).status(HttpStatus.BAD_REQUEST.value()).message("Malformed JSON request").build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
