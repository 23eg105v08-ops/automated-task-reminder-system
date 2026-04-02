package com.dinesh.demo.service;

import com.dinesh.demo.model.Task;
import com.dinesh.demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private static final double HIGH_RISK_THRESHOLD = 0.20;
    private static final double MEDIUM_RISK_THRESHOLD = 0.50;

    private final TaskRepository taskRepository;
    private final SmsNotificationService smsNotificationService;
    private final EmailNotificationService emailNotificationService;
    private final WebNotificationService webNotificationService;

    public List<Task> findAll() {
        refreshTimeBasedTaskState();
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id) {
        refreshTimeBasedTaskState();
        return taskRepository.findById(id);
    }

    public Task save(Task task) {
        if (task.getStatus() == null || task.getStatus().isBlank()) {
            task.setStatus("PENDING");
        }

        applyTimeBasedState(task, LocalDateTime.now());
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findByStatus(String status) {
        refreshTimeBasedTaskState();
        return taskRepository.findByStatus(status);
    }

    public List<Task> findByPriority(String priority) {
        refreshTimeBasedTaskState();
        return taskRepository.findByPriority(priority);
    }

    public List<Task> findPendingTasks() {
        refreshTimeBasedTaskState();
        return taskRepository.findPendingTasksOrderedByDueDate();
    }

    public List<Task> findTasksForReminder(LocalDateTime time) {
        refreshTimeBasedTaskState();
        return taskRepository.findTasksForReminder(time);
    }

    public List<Task> findOverdueTasks() {
        refreshTimeBasedTaskState();
        return taskRepository.findByStatus("OVERDUE");
    }

    public long getTotalTasksCount() {
        return taskRepository.count();
    }

    public long getPendingTasksCount() {
        refreshTimeBasedTaskState();
        return taskRepository.findByStatus("PENDING").size();
    }

    public long getCompletedTasksCount() {
        return taskRepository.findByStatus("COMPLETED").size();
    }

    public long getOverdueTasksCount() {
        refreshTimeBasedTaskState();
        return taskRepository.findByStatus("OVERDUE").size();
    }

    public void markAsCompleted(Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setStatus("COMPLETED");
            taskRepository.save(task);
        });
    }

    /** Called on page load: silently updates DB priority/status, no SMS. */
    public synchronized int refreshTimeBasedTaskState() {
        return doRefresh(false);
    }

    /** Called by scheduler every minute: updates DB AND sends SMS alerts. */
    public synchronized int escalateAndNotify() {
        return doRefresh(true);
    }

    private int doRefresh(boolean sendNotifications) {
        List<Task> pendingTasks = taskRepository.findByStatus("PENDING");
        if (pendingTasks.isEmpty()) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();
        List<Task> changedTasks = new ArrayList<>();
        List<EscalationEvent> events = new ArrayList<>();

        for (Task task : pendingTasks) {
            EscalationResult result = computeEscalation(task, now);
            if (result.changed()) {
                if (result.becameOverdue()) {
                    task.setStatus("OVERDUE");
                } else {
                    task.setPriority(result.newPriority());
                }
                changedTasks.add(task);
                events.add(new EscalationEvent(
                    task.getTitle(), result.becameOverdue(), result.newPriority()));
            }
        }

        if (!changedTasks.isEmpty()) {
            taskRepository.saveAll(changedTasks);
            if (sendNotifications) {
                for (EscalationEvent event : events) {
                    String level = event.becameOverdue() ? "OVERDUE" : event.newPriority();
                    smsNotificationService.sendEscalationAlert(
                        event.taskTitle(),
                        level,
                        event.becameOverdue()
                    );
                    emailNotificationService.sendEscalationAlert(
                        event.taskTitle(),
                        level,
                        event.becameOverdue()
                    );
                    webNotificationService.publishEscalation(
                        event.taskTitle(),
                        level,
                        event.becameOverdue()
                    );
                }
            }
        }

        return changedTasks.size();
    }

    private boolean applyTimeBasedState(Task task, LocalDateTime now) {
        EscalationResult result = computeEscalation(task, now);
        if (!result.changed()) {
            return false;
        }
        if (result.becameOverdue()) {
            task.setStatus("OVERDUE");
        } else {
            task.setPriority(result.newPriority());
        }
        return true;
    }

    private EscalationResult computeEscalation(Task task, LocalDateTime now) {
        if (!"PENDING".equalsIgnoreCase(task.getStatus())) {
            return EscalationResult.unchanged();
        }

        LocalDateTime dueDate = task.getDueDate();
        if (dueDate == null) {
            return EscalationResult.unchanged();
        }

        // Due date has passed → OVERDUE
        if (!dueDate.isAfter(now)) {
            return EscalationResult.overdue();
        }

        String currentPriority = normalizePriority(task.getPriority());

        // Reminder time reached → force HIGH risk
        LocalDateTime reminderTime = task.getReminderTime();
        String computedPriority;
        if (reminderTime != null && !reminderTime.isAfter(now)) {
            computedPriority = "HIGH";
        } else {
            computedPriority = computePriorityByTime(task, now, dueDate);
        }

        // Escalation is one-way: never downgrade
        String escalatedPriority = riskRank(computedPriority) > riskRank(currentPriority)
            ? computedPriority
            : currentPriority;

        if (!escalatedPriority.equals(currentPriority)) {
            return EscalationResult.priorityChange(escalatedPriority);
        }

        return EscalationResult.unchanged();
    }

    private String computePriorityByTime(Task task, LocalDateTime now, LocalDateTime dueDate) {
        LocalDateTime created = task.getCreatedDate();
        if (created == null || !created.isBefore(dueDate)) {
            return "HIGH";
        }

        long totalSeconds = Duration.between(created, dueDate).getSeconds();
        if (totalSeconds <= 0) {
            return "HIGH";
        }

        long remainingSeconds = Duration.between(now, dueDate).getSeconds();
        double remainingRatio = remainingSeconds / (double) totalSeconds;

        if (remainingRatio <= HIGH_RISK_THRESHOLD) {
            return "HIGH";
        }
        if (remainingRatio <= MEDIUM_RISK_THRESHOLD) {
            return "MEDIUM";
        }
        return "LOW";
    }

    private String normalizePriority(String priority) {
        if (priority == null) {
            return "LOW";
        }

        String normalized = priority.trim().toUpperCase();
        if (normalized.equals("EASY")) {
            return "LOW";
        }
        if (!normalized.equals("LOW") && !normalized.equals("MEDIUM") && !normalized.equals("HIGH")) {
            return "LOW";
        }
        return normalized;
    }

    private int riskRank(String priority) {
        return switch (priority) {
            case "HIGH" -> 3;
            case "MEDIUM" -> 2;
            default -> 1;
        };
    }

    private record EscalationResult(boolean changed, boolean becameOverdue, String newPriority) {
        static EscalationResult unchanged()           { return new EscalationResult(false, false, null); }
        static EscalationResult overdue()             { return new EscalationResult(true,  true,  "OVERDUE"); }
        static EscalationResult priorityChange(String p) { return new EscalationResult(true, false, p); }
    }

    private record EscalationEvent(String taskTitle, boolean becameOverdue, String newPriority) {}
}
