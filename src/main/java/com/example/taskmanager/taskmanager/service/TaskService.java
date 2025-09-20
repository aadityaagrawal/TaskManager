package com.example.taskmanager.taskmanager.service;

import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;

import com.example.taskmanager.taskmanager.model.RequestTaskModel;
import com.example.taskmanager.taskmanager.model.TaskModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public interface TaskService {

    public String deleteTask(Long id);
    public TaskModel getTaskById(Long id);
    public String addTask(RequestTaskModel taskModel);
    public String updateTask(Long id, RequestTaskModel updatedTask);
    public String patchStatus(Long Id, Status status) ;
    public Page<TaskModel> findAllData(Status status, Boolean dueDate, Priority priority, Pageable pageable);
    public Map<String, Object> getTaskStatusAndPriorityCount();
}
