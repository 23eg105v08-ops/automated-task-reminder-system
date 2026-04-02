package com.dinesh.demo.controller;

import com.dinesh.demo.model.WebNotification;
import com.dinesh.demo.service.WebNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final WebNotificationService webNotificationService;

    @GetMapping("/after")
    public List<WebNotification> after(@RequestParam(defaultValue = "0") long afterId) {
        return webNotificationService.findAfter(afterId);
    }

    @GetMapping("/latest-id")
    public Map<String, Long> latestId() {
        return Map.of("latestId", webNotificationService.latestId());
    }
}
