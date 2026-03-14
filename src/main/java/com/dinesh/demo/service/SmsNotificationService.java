package com.dinesh.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@Service
@Slf4j
public class SmsNotificationService {

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;

    @Value("${twilio.from-number:}")
    private String fromNumber;

    @Value("${notification.to-number:}")
    private String toNumber;

    /**
     * Sends an SMS alert when a task is escalated to a higher risk level or becomes OVERDUE.
     *
     * @param taskTitle    the name of the task
     * @param newLevel     "MEDIUM", "HIGH", or "OVERDUE"
     * @param becameOverdue true when the task just crossed the due date
     */
    public void sendEscalationAlert(String taskTitle, String newLevel, boolean becameOverdue) {
        if (accountSid.isBlank() || authToken.isBlank() || fromNumber.isBlank() || toNumber.isBlank()) {
            log.warn("[SMS] Twilio not configured – skipping notification for task: {} → {}", taskTitle, newLevel);
            return;
        }

        String body;
        if (becameOverdue) {
            body = "⚠️ OVERDUE ALERT: Task \"" + taskTitle
                + "\" has passed its due time and is now OVERDUE. Please take action immediately.";
        } else if ("HIGH".equals(newLevel)) {
            body = "🔴 HIGH RISK: Task \"" + taskTitle
                + "\" is critically close to its deadline. Immediate attention required!";
        } else {
            body = "🟡 MEDIUM RISK: Task \"" + taskTitle
                + "\" is approaching its deadline. Please prioritize it.";
        }

        String url = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Messages.json";
        String formData = "From=" + encode(fromNumber)
            + "&To=" + encode(toNumber)
            + "&Body=" + encode(body);

        String credentials = Base64.getEncoder()
            .encodeToString((accountSid + ":" + authToken).getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(15))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic " + credentials)
            .POST(HttpRequest.BodyPublishers.ofString(formData))
            .build();

        try {
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("[SMS] ✓ Notification sent for task '{}' → {}", taskTitle, newLevel);
            } else {
                log.error("[SMS] ✗ Failed. HTTP {}: {}", response.statusCode(), response.body());
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.error("[SMS] Interrupted while sending notification for task '{}'", taskTitle);
        } catch (Exception ex) {
            log.error("[SMS] Exception while sending notification for task '{}'", taskTitle, ex);
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
