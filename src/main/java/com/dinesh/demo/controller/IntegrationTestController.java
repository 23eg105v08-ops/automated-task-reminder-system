package com.dinesh.demo.controller;

import com.dinesh.demo.service.AiAssistantService;
import com.dinesh.demo.service.EmailNotificationService;
import com.dinesh.demo.service.SmsNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/integrations/test")
@RequiredArgsConstructor
public class IntegrationTestController {

    private final AiAssistantService aiAssistantService;
    private final SmsNotificationService smsNotificationService;
    private final EmailNotificationService emailNotificationService;

    @Value("${twilio.account-sid:}")
    private String twilioSid;

    @Value("${twilio.auth-token:}")
    private String twilioToken;

    @Value("${twilio.from-number:}")
    private String twilioFrom;

    @Value("${notification.to-number:}")
    private String twilioTo;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    @Value("${notification.to-email:}")
    private String mailTo;

    @PostMapping("/ai")
    public Map<String, String> testAi(@RequestBody(required = false) Map<String, String> payload) {
        String question = payload != null
            ? payload.getOrDefault("question", "Suggest 3 high-impact actions for today")
            : "Suggest 3 high-impact actions for today";
        String answer = aiAssistantService.ask(question);
        return Map.of(
            "status", "ok",
            "message", "AI endpoint responded successfully",
            "answer", answer
        );
    }

    @PostMapping("/sms")
    public Map<String, String> testSms() {
        if (isBlank(twilioSid) || isBlank(twilioToken) || isBlank(twilioFrom) || isBlank(twilioTo)) {
            return Map.of(
                "status", "error",
                "message", "SMS config missing. Set TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN, TWILIO_FROM_NUMBER, NOTIFICATION_PHONE."
            );
        }

        smsNotificationService.sendEscalationAlert("Integration Test Task", "HIGH", false);
        return Map.of(
            "status", "ok",
            "message", "SMS test triggered. Check your phone and server logs."
        );
    }

    @PostMapping("/email-overdue")
    public Map<String, String> testOverdueEmail() {
        if (isBlank(mailFrom) || isBlank(mailTo)) {
            return Map.of(
                "status", "error",
                "message", "Email config missing. Set MAIL_USERNAME and NOTIFICATION_EMAIL (plus SMTP host/password)."
            );
        }

        emailNotificationService.sendEscalationAlert("Integration Test Task", "OVERDUE", true);
        return Map.of(
            "status", "ok",
            "message", "Overdue email test triggered. Check inbox/spam and server logs."
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
