package com.example.taskmanager.taskmanager.controller;

import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;
import com.example.taskmanager.taskmanager.model.RequestTaskModel;
import com.example.taskmanager.taskmanager.model.TaskModel;
import com.example.taskmanager.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Operation(summary = "Retrieve all tasks", description = "Get a paginated list of tasks, optionally filtered by status, priority, or due date")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Tasks retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskModel.class))), @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTasks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Status status, @RequestParam(required = false) Priority priority, @RequestParam(required = false) Boolean dueDate) {
        logger.info("Fetching all tasks");

        Pageable pageable = PageRequest.of(page, size);
        Page<TaskModel> tasksPage = taskService.findAllData(status, dueDate, priority, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("tasks", tasksPage.getContent());
        response.put("currentPage", tasksPage.getNumber());
        response.put("totalItems", tasksPage.getTotalElements());
        response.put("totalPages", tasksPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retrieve a task by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTaskById(@PathVariable Long id) {
        logger.info("Fetching task by ID: {}", id);

        TaskModel task = taskService.getTaskById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("task", task);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new task")
    @PostMapping
    public ResponseEntity<Map<String, Object>> addTask(@Valid @RequestBody RequestTaskModel requestTaskModel) {
        logger.info("Adding new task");

        String message = taskService.addTask(requestTaskModel);
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update task details")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTask(@PathVariable Long id, @Valid @RequestBody RequestTaskModel requestTaskModel) {
        logger.info("Updating task with ID: {}", id);

        String message = taskService.updateTask(id, requestTaskModel);
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update task status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> patchStatus(@PathVariable Long id, @RequestParam Status status) {
        logger.info("Updating status for task ID: {}", id);

        String message = taskService.patchStatus(id, status);
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTask(@PathVariable Long id) {
        logger.info("Deleting task with ID: {}", id);

        String message = taskService.deleteTask(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get task statistics")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getTaskStats() {
        logger.info("Fetching task statistics");

        Map<String, Object> stats = taskService.getTaskStatusAndPriorityCount();
        return ResponseEntity.ok(stats);
    }
}
