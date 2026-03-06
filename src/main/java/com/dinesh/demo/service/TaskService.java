package com.dinesh.demo.service;

import com.dinesh.demo.model.Task;
import com.dinesh.demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Task save(Task task) {
        // Update overdue status
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now()) 
            && "PENDING".equals(task.getStatus())) {
            task.setStatus("OVERDUE");
        }
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> findByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    public List<Task> findPendingTasks() {
        return taskRepository.findPendingTasksOrderedByDueDate();
    }

    public List<Task> findTasksForReminder(LocalDateTime time) {
        return taskRepository.findTasksForReminder(time);
    }

    public List<Task> findOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDateTime.now());
    }

    public long getTotalTasksCount() {
        return taskRepository.count();
    }

    public long getPendingTasksCount() {
        return taskRepository.findByStatus("PENDING").size();
    }

    public long getCompletedTasksCount() {
        return taskRepository.findByStatus("COMPLETED").size();
    }

    public long getOverdueTasksCount() {
        return findOverdueTasks().size();
    }

    public void markAsCompleted(Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setStatus("COMPLETED");
            taskRepository.save(task);
        });
    }
}
