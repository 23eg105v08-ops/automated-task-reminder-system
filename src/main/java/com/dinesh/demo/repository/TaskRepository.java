package com.dinesh.demo.repository;

import com.dinesh.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByStatus(String status);
    
    List<Task> findByPriority(String priority);
    
    @Query("SELECT t FROM Task t WHERE t.status = 'PENDING' ORDER BY t.dueDate ASC")
    List<Task> findPendingTasksOrderedByDueDate();
    
    @Query("SELECT t FROM Task t WHERE t.reminderTime <= ?1 AND t.status = 'PENDING'")
    List<Task> findTasksForReminder(LocalDateTime time);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate < ?1 AND t.status = 'PENDING'")
    List<Task> findOverdueTasks(LocalDateTime time);
}
