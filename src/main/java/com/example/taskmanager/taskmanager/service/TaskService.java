package com.example.taskmanager.taskmanager.service;

import com.example.taskmanager.taskmanager.entity.TaskEntity;
import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;
import com.example.taskmanager.taskmanager.exception.CustomExceptions;
import com.example.taskmanager.taskmanager.model.TaskModel;
import com.example.taskmanager.taskmanager.repository.TaskRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Service
public class TaskService {

    @Autowired
    TaskRepo taskRepo;

    public String deleteTask(Long id) {

        taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));

        if (taskRepo.findStatusById(id) == Status.IN_PROGRESS) {
            throw new CustomExceptions.TaskDeletionException("Cannot delete task with status IN_PROGRESS");
        }
        taskRepo.deleteById(id);
        return "Task Deleted Successfully";

    }

    public TaskModel getTaskById(Long id) {

        try {
            TaskEntity taskEntity = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));
            TaskModel taskModel = new TaskModel();
            taskModel.setId(taskEntity.getId());
            taskModel.setTitle(taskEntity.getTitle());
            taskModel.setDescription(taskEntity.getDescription());
            taskModel.setStatus(taskEntity.getStatus());
            taskModel.setPriority(taskEntity.getPriority());
            taskModel.setDueDate(taskEntity.getDueDate());
            taskModel.setCreatedAt(taskEntity.getCreatedAt());
            taskModel.setUpdatedAt(taskEntity.getUpdatedAt());
            return taskModel;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public String addTask(TaskModel taskModel) {
        try {
            validateDueDate(taskModel.getDueDate());
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setTitle(taskModel.getTitle());
            taskEntity.setDescription(taskModel.getDescription());
            taskEntity.setStatus(taskModel.getStatus());
            taskEntity.setPriority(taskModel.getPriority());
            taskEntity.setDueDate(taskModel.getDueDate());
            taskRepo.save(taskEntity);
            return "Task added successfully";
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Task with the same title and status already exists.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public String updateTask(Long id, TaskModel updatedTask) {

        try {
            TaskEntity taskEntity = taskRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Task with id " + id + " not found"));

            taskEntity.setTitle(updatedTask.getTitle());
            taskEntity.setDescription(updatedTask.getDescription());
            taskEntity.setStatus(updatedTask.getStatus());
            taskEntity.setPriority(updatedTask.getPriority());
            taskEntity.setDueDate(updatedTask.getDueDate());

            validateDueDate(updatedTask.getDueDate());

            taskRepo.save(taskEntity);
            return "Task updated successfully";
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Task with the same title and status already exists.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String patchStatus(Long Id, Status status) {

        try {
            TaskEntity taskEntity = taskRepo.findById(Id).orElseThrow(() -> new NoSuchElementException("Task with id " + Id + " not found"));
            taskEntity.setStatus(status);
            taskRepo.save(taskEntity);
            return "Status Updated Successfully";
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Task with the same title and status already exists.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public List<TaskModel> findAllData(Status status, Boolean dueDate, Priority priority) {
        try {

            List<TaskModel> allTasks = taskRepo.findAll(status, priority, dueDate).stream().map(entity -> {
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
            }).toList();

            return allTasks;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getTaskStatusAndPriorityCount() {

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("Not OverdueCount", taskRepo.countByDueDateAfterAndStatusNot(LocalDate.now(), Status.COMPLETED));
            stats.put("OverdueCount", taskRepo.countByDueDateBeforeAndStatusNot(LocalDate.now(), Status.COMPLETED));
            stats.put("detailedStats", taskRepo.getTaskStatusAndPriorityCount());
            return stats;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void validateDueDate(LocalDate dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be before today");
        }
    }

}
