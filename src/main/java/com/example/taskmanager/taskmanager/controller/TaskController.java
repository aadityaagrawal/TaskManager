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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * REST controller for managing tasks.
 * Provides endpoints to create, retrieve, update, and delete tasks.
 */
@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    /**
     * Retrieves all tasks, optionally filtered by status, priority, or due date.
     *
     * @param status   Optional status to filter tasks.
     * @param priority Optional priority to filter tasks.
     * @param dueDate  Optional flag to filter by due date.
     * @return ResponseEntity containing a map with the list of tasks.
     */
    @Operation(summary = "Retrieve all tasks", description = "Gets a list of all tasks in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request due to invalid filters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })

    @GetMapping("/tasks")
    public ResponseEntity<Map<String, Object>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Boolean dueDate) {

        logger.info("Fetching all the tasks.............");

        Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());

        // ✅ Service should return Page<TaskModel>
        Page<TaskModel> pageTasks = taskService.findAllData(status, dueDate, priority, pageable);

        // ✅ Build response map
        Map<String, Object> response = new HashMap<>();
        response.put("tasks", pageTasks.getContent());       // actual list
        response.put("currentPage", pageTasks.getNumber());
        response.put("totalItems", pageTasks.getTotalElements());
        response.put("totalPages", pageTasks.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Map<String,Object>> getTaskById(@PathVariable Long id) {
        logger.info("Fetching the task by id.............");
        TaskModel taskModel = taskService.getTaskById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("task", taskModel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Add a new task", description = "Adds a new task to the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request due to invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/tasks")
    ResponseEntity<Map<String,Object>> addTask(
            @RequestBody @Valid RequestTaskModel requestTaskModel) {
        logger.info("Adding the task.............");

        String message = taskService.addTask(requestTaskModel);

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates the status of an existing task.
     *
     * @param Id     ID of the task to update.
     * @param status New status to set.
     * @return ResponseEntity with a success or not found message.
     */
    @Operation(summary = "Update task status", description = "Updates the status of an existing task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found with the given ID", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request due to invalid parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PatchMapping("/tasks/{id}/status")
    ResponseEntity<String> patchStatus(@PathVariable Long Id, @RequestParam Status status) {
        logger.info("Updating the status of the task.............");
        String response = taskService.patchStatus(Id, status);
        if (response.equals("Status Updated Successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Update an existing task", description = "Updates the details of an existing task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request due to invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found with the given ID", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PutMapping("/tasks/{id}")
    ResponseEntity<Map<String,Object>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody RequestTaskModel taskModel) {
        logger.info("Updating the task.............");

        String message = taskService.updateTask(id, taskModel);

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id ID of the task to delete.
     * @return ResponseEntity containing a map with a deletion message.
     */
    @Operation(summary = "Delete a task", description = "Deletes a task by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @DeleteMapping("/tasks/{id}")
    ResponseEntity<Map<String, Object>> deleteTask(@PathVariable Long id) {
        logger.info("Deleting the task.............");
        String response = taskService.deleteTask(id);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/tasks/stats")
    ResponseEntity<Map<String, Object>> getTaskStats() {
        logger.info("Fetching the task statistics.............");
        Map<String,Object> response = taskService.getTaskStatusAndPriorityCount();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
