package com.example.taskmanager.taskmanager.utility;

import com.example.taskmanager.taskmanager.entity.TaskEntity;
import com.example.taskmanager.taskmanager.model.RequestTaskModel;
import com.example.taskmanager.taskmanager.model.TaskModel;

import java.time.LocalDateTime;

public class MapperClass {
    // --- Mapper Helpers ---
    public static TaskModel mapToModel(TaskEntity entity) {
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

    public static TaskEntity mapToEntity(RequestTaskModel taskModel) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(taskModel.getTitle());
        entity.setDescription(taskModel.getDescription());
        entity.setStatus(taskModel.getStatus());
        entity.setPriority(taskModel.getPriority());
        entity.setDueDate(taskModel.getDueDate());
        return entity;
    }
}
