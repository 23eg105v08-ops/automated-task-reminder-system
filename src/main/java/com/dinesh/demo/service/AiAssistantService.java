package com.dinesh.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AiAssistantService {

    private final ObjectMapper objectMapper;

    @Value("${ai.gemini.base-url}")
    private String baseUrl;

    @Value("${ai.gemini.model}")
    private String model;

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    public String ask(String question) {
        if (question == null || question.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question is required");
        }

        String cleanQuestion = question.trim();

        if (apiKey == null || apiKey.isBlank()) {
            return buildFallbackAnswer(cleanQuestion);
        }

        String encodedApiKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
        String resolvedModel = model != null && model.startsWith("models/")
            ? model.substring("models/".length())
            : model;
        String endpoint = baseUrl + "/models/" + resolvedModel + ":generateContent?key=" + encodedApiKey;
        String payload = buildPayload(cleanQuestion);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .timeout(Duration.ofSeconds(30))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build();

        try {
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw mapProviderError(response.statusCode(), response.body());
            }

            JsonNode root = objectMapper.readTree(response.body());
            String content = extractAssistantText(root);
            if (content == null || content.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI provider returned no content");
            }

            return content;
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to call AI provider", ex);
        }
    }

    private String buildPayload(String question) {
        ObjectNode root = objectMapper.createObjectNode();
        ObjectNode generationConfig = root.putObject("generationConfig");
        generationConfig.put("temperature", 0.4);

        ArrayNode contents = root.putArray("contents");
        ObjectNode userContent = contents.addObject();
        userContent.put("role", "user");
        ArrayNode parts = userContent.putArray("parts");
        parts.addObject().put("text",
            "You help with task planning, prioritization, and reminders. Keep answers concise and actionable.\n\n"
                + question);

        return root.toString();
    }

    private String extractAssistantText(JsonNode root) {
        JsonNode responseText = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
        if (!responseText.isMissingNode() && !responseText.asText().isBlank()) {
            return responseText.asText().trim();
        }

        return null;
    }

    private String buildFallbackAnswer(String question) {
        return "AI provider is not configured yet (missing GEMINI_API_KEY). "
            + "Quick plan for your task: 1) break it into 3 small steps, "
            + "2) set a due time for each step, 3) start the highest impact step first. "
            + "Your question was: " + question;
    }

    private ResponseStatusException mapProviderError(int statusCode, String body) {
        String details = "AI provider error (" + statusCode + ")";
        if (body != null && !body.isBlank()) {
            details = details + ": " + body;
        }

        if (statusCode == 429) {
            return new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                "Gemini quota exceeded. Check your Gemini usage limits and billing.");
        }
        if (statusCode == 401 || statusCode == 403) {
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Gemini API key is invalid or lacks permission.");
        }
        if (statusCode == 400 || statusCode == 404) {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Gemini request/model configuration is invalid.");
        }

        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, details);
    }
}
