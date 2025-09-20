package com.example.taskmanager.taskmanager.repository;

import com.example.taskmanager.taskmanager.entity.TaskEntity;
import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<TaskEntity, Long> {

    @Query("SELECT t FROM TaskEntity t WHERE (:STATUS IS NULL OR t.status = :STATUS) AND (:PRIORITY IS NULL OR t.priority = :PRIORITY) AND (:DUE_DATE IS NULL OR (:DUE_DATE = TRUE AND t.dueDate < CURRENT_TIMESTAMP and t.status <> 'COMPLETED') OR (:DUE_DATE = FALSE AND t.dueDate > CURRENT_TIMESTAMP AND t.status <> 'COMPLETED'))")
    Page<TaskEntity> findAll(@Param("STATUS") Status status, @Param("PRIORITY") Priority priority, @Param("DUE_DATE") Boolean dueDate, Pageable pageable);

    @Query("select t.status, t.priority , count(*) from TaskEntity t group by t.status, t.priority")
    List<Object[]> getTaskStatusAndPriorityCount();

    Long countByDueDateBeforeAndStatusNot(LocalDateTime dueDate, Status status);
    Long countByDueDateAfterAndStatusNot(LocalDateTime dueDate, Status status);

}
