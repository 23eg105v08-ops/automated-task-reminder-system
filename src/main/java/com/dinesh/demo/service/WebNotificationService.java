package com.dinesh.demo.service;

import com.dinesh.demo.model.WebNotification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WebNotificationService {

    private static final int MAX_NOTIFICATIONS = 200;

    private final AtomicLong idSequence = new AtomicLong();
    private final Deque<WebNotification> notifications = new ConcurrentLinkedDeque<>();

    public void publishEscalation(String taskTitle, String newLevel, boolean becameOverdue) {
        String level = becameOverdue ? "OVERDUE" : newLevel;
        String message;

        if (becameOverdue) {
            message = "Task \"" + taskTitle + "\" is now OVERDUE. Immediate action is required.";
        } else if ("HIGH".equalsIgnoreCase(newLevel)) {
            message = "Task \"" + taskTitle + "\" has escalated to HIGH priority.";
        } else {
            message = "Task \"" + taskTitle + "\" has escalated to MEDIUM priority.";
        }

        long id = idSequence.incrementAndGet();
        notifications.addLast(new WebNotification(id, level, taskTitle, message, LocalDateTime.now()));
        trimIfNeeded();
    }

    public List<WebNotification> findAfter(long afterId) {
        return notifications.stream()
            .filter(notification -> notification.id() > afterId)
            .sorted(Comparator.comparingLong(WebNotification::id))
            .toList();
    }

    public long latestId() {
        WebNotification last = notifications.peekLast();
        return last != null ? last.id() : 0L;
    }

    private void trimIfNeeded() {
        while (notifications.size() > MAX_NOTIFICATIONS) {
            notifications.pollFirst();
        }
    }
}
