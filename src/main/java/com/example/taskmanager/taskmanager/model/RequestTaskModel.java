package com.example.taskmanager.taskmanager.model;

import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class RequestTaskModel {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull(message = "Status cannot be null")
    private Status status;

    @NotNull(message = "Priority cannot be null")
    private Priority priority;

    @NotNull(message = "Due date cannot be null")
    @Schema(description = "Due date of the task in YYYY-MM-DD format", example = "YYYY-MM-DD")
    private LocalDateTime dueDate;
}
