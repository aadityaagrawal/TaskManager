package com.example.taskmanager.taskmanager.repository;

import com.example.taskmanager.taskmanager.entity.TaskEntity;
import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<TaskEntity, Long> {

    @Query("SELECT t FROM TaskEntity t WHERE (:STATUS IS NULL OR t.status = :STATUS) AND (:PRIORITY IS NULL OR t.priority = :PRIORITY) AND (:DUE_DATE IS NULL OR (:DUE_DATE = TRUE AND t.dueDate < CURRENT_TIMESTAMP) OR (:DUE_DATE = FALSE AND t.dueDate < CURRENT_TIMESTAMP AND t.status <> 'COMPLETED'))")
    List<TaskEntity> findAll(@Param("STATUS") Status status, @Param("PRIORITY") Priority priority, @Param("DUE_DATE") Boolean dueDate);

    @Query("SELECT t.status from TaskEntity t WHERE t.id = :ID")
    Status findStatusById(@Param("ID") Long id);

}
