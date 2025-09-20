package com.example.taskmanager.taskmanager.model;

import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestTaskModel {
    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @NotBlank
    private Status status;

    @NotBlank
    private Priority priority;

    @NotBlank
    @Schema(description = "Due date of the task in YYYY-MM-DD format", example = "YYYY-MM-DD")
    private LocalDate dueDate;
}
