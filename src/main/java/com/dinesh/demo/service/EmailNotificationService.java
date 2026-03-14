package com.dinesh.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${notification.to-email:}")
    private String toEmail;

    public EmailNotificationService(ObjectProvider<JavaMailSender> mailSenderProvider) {
        this.mailSender = mailSenderProvider.getIfAvailable();
    }

    public void sendEscalationAlert(String taskTitle, String newLevel, boolean becameOverdue) {
        if (mailSender == null) {
            log.warn("[EMAIL] Mail sender bean not available - configure spring.mail.host/username/password to enable email alerts");
            return;
        }

        if (toEmail == null || toEmail.isBlank() || fromEmail == null || fromEmail.isBlank()) {
            log.warn("[EMAIL] Email config is missing - skipping notification for task: {} -> {}", taskTitle, newLevel);
            return;
        }

        String subject;
        String body;

        if (becameOverdue) {
            subject = "Overdue task alert: " + taskTitle;
            body = "Task \"" + taskTitle + "\" is now OVERDUE. Please take action immediately.";
        } else if ("HIGH".equalsIgnoreCase(newLevel)) {
            subject = "High risk task alert: " + taskTitle;
            body = "Task \"" + taskTitle + "\" has escalated to HIGH risk and needs immediate attention.";
        } else {
            subject = "Medium risk task alert: " + taskTitle;
            body = "Task \"" + taskTitle + "\" has escalated to MEDIUM risk. Please prioritize it.";
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("[EMAIL] Notification sent for task '{}' -> {}", taskTitle, newLevel);
        } catch (Exception ex) {
            log.error("[EMAIL] Failed to send notification for task '{}'", taskTitle, ex);
        }
    }
}
