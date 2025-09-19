package com.example.taskmanager.taskmanager.entity;


import com.example.taskmanager.taskmanager.enums.Priority;
import com.example.taskmanager.taskmanager.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "task", uniqueConstraints = @UniqueConstraint(columnNames = {"title", "status"}))
@Getter
@Setter
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq_gen")
    @SequenceGenerator(name = "task_seq_gen", sequenceName = "task_seq", allocationSize = 10, initialValue = 11)
    private Long id;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "description", length = 500)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;
    @Column(name = "due_date")
    private LocalDate dueDate;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
