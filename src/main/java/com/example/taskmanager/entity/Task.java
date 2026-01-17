package com.example.taskmanager.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private boolean completed;
    private LocalDate dueDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
