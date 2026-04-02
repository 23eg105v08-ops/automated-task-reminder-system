package com.dinesh.demo.model;

import java.time.LocalDateTime;

public record WebNotification(
    long id,
    String level,
    String taskTitle,
    String message,
    LocalDateTime createdAt
) {
}
