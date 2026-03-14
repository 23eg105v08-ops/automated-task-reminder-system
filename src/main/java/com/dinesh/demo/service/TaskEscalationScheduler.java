package com.dinesh.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskEscalationScheduler {

    private final TaskService taskService;

    @Scheduled(fixedDelayString = "${task.escalation.fixed-delay-ms:60000}")
    public void runEscalation() {
        taskService.escalateAndNotify();
    }
}
