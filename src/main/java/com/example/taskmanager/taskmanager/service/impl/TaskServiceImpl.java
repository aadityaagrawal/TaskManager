package com.example.taskmanager.taskmanager.service.impl;

import com.example.taskmanager.taskmanager.entity.TaskEntity;
import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;
import com.example.taskmanager.taskmanager.exception.CustomExceptions;
import com.example.taskmanager.taskmanager.model.RequestTaskModel;
import com.example.taskmanager.taskmanager.model.TaskModel;
import com.example.taskmanager.taskmanager.repository.TaskRepo;
import com.example.taskmanager.taskmanager.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepo taskRepo;

    // --- Mapper Helpers ---
    private TaskModel mapToModel(TaskEntity entity) {
        TaskModel model = new TaskModel();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setStatus(entity.getStatus());
        model.setPriority(entity.getPriority());
        model.setDueDate(entity.getDueDate());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    private TaskEntity mapToEntity(RequestTaskModel taskModel) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(taskModel.getTitle());
        entity.setDescription(taskModel.getDescription());
        entity.setStatus(taskModel.getStatus());
        entity.setPriority(taskModel.getPriority());
        entity.setDueDate(taskModel.getDueDate());
        return entity;
    }

    // --- CRUD Operations ---

    public TaskModel getTaskById(Long id) {
        TaskEntity task = taskRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));
        return mapToModel(task);
    }

    @Transactional
    public String addTask(RequestTaskModel taskModel) {
        validateDueDate(taskModel.getDueDate());
        try {
            taskRepo.save(mapToEntity(taskModel));
            return "Task added successfully";
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Task with the same title and status already exists.");
        }
    }

    @Transactional
    public String updateTask(Long id, RequestTaskModel updatedTask) {
        TaskEntity task = taskRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());
        task.setPriority(updatedTask.getPriority());
        task.setDueDate(updatedTask.getDueDate());

        validateDueDate(updatedTask.getDueDate());

        try {
            taskRepo.save(task);
            return "Task updated successfully";
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Task with the same title and status already exists.");
        }
    }

    public String deleteTask(Long id) {
        TaskEntity task = taskRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));

        if (task.getStatus() == Status.IN_PROGRESS) {
            throw new CustomExceptions.TaskDeletionException("Cannot delete task with status IN_PROGRESS");
        }

        taskRepo.delete(task);
        return "Task Deleted Successfully";
    }

    @Transactional
    public String patchStatus(Long id, Status status) {
        TaskEntity task = taskRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));
        task.setStatus(status);
        taskRepo.save(task);
        return "Status Updated Successfully";
    }

    // --- Pagination & Filtering ---
    public Page<TaskModel> findAllData(Status status, Boolean dueDate, Priority priority, Pageable pageable) {
        Page<TaskEntity> entitiesPage = taskRepo.findAll(status, priority, dueDate, pageable);
        return entitiesPage.map(this::mapToModel);
    }

    // --- Statistics ---
    public Map<String, Object> getTaskStatusAndPriorityCount() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("Not_Overdue_Count", taskRepo.countByDueDateAfterAndStatusNot(LocalDate.now(), Status.COMPLETED));
        stats.put("Overdue_Count", taskRepo.countByDueDateBeforeAndStatusNot(LocalDate.now(), Status.COMPLETED));
        stats.put("Detailed_Stats", taskRepo.getTaskStatusAndPriorityCount());
        return stats;
    }

    // --- Validation ---
    private void validateDueDate(LocalDate dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be before today");
        }
    }
}
