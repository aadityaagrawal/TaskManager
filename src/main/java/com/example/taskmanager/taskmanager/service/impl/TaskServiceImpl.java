package com.example.taskmanager.taskmanager.service.impl;

import com.example.taskmanager.taskmanager.entity.TaskEntity;
import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;
import com.example.taskmanager.taskmanager.exception.CustomExceptions;
import com.example.taskmanager.taskmanager.model.RequestTaskModel;
import com.example.taskmanager.taskmanager.model.TaskModel;
import com.example.taskmanager.taskmanager.repository.TaskRepo;
import com.example.taskmanager.taskmanager.service.TaskService;
import com.example.taskmanager.taskmanager.utility.MapperClass;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.example.taskmanager.taskmanager.utility.MapperClass.*;


@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepo taskRepo;

    // --- CRUD Operations ---

    public TaskModel getTaskById(Long id) {
        TaskEntity task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));
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
        TaskEntity task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));

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
        TaskEntity task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));

        if (task.getStatus() == Status.IN_PROGRESS) {
            throw new CustomExceptions.TaskDeletionException("Cannot delete task with status IN_PROGRESS");
        }

        taskRepo.delete(task);
        return "Task Deleted Successfully";
    }

    @Transactional
    public String patchStatus(Long id, Status status) {
        TaskEntity task = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));
        task.setStatus(status);
        taskRepo.save(task);
        return "Status Updated Successfully";
    }

    // --- Pagination & Filtering ---
    public Page<TaskModel> findAllData(Status status, Boolean dueDate, Priority priority, Pageable pageable) {
        Page<TaskEntity> entitiesPage = taskRepo.findAll(status, priority, dueDate, pageable);
        return entitiesPage.map(MapperClass::mapToModel);
    }

    // --- Statistics ---
    public Map<String, Object> getTaskStatusAndPriorityCount() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("Not_Overdue_Count", taskRepo.countByDueDateAfterAndStatusNot(LocalDateTime.now(), Status.COMPLETED));
        stats.put("Overdue_Count", taskRepo.countByDueDateBeforeAndStatusNot(LocalDateTime.now(), Status.COMPLETED));
        stats.put("Detailed_Stats", taskRepo.getTaskStatusAndPriorityCount());
        return stats;
    }
    // --- Validation ---
    public void validateDueDate(LocalDateTime dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date cannot be before today");
        }
    }
}
