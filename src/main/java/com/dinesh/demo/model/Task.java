package com.dinesh.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    private String description;
    
    private LocalDateTime dueDate;
    
    private LocalDateTime reminderTime;
    
    private String priority; // LOW, MEDIUM, HIGH
    
    private String status; // PENDING, COMPLETED, OVERDUE
    
    private LocalDateTime createdDate;

    @PrePersist
    public void onCreate() {
        createdDate = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }
}
