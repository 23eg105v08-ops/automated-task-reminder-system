package com.dinesh.demo.controller;

import com.dinesh.demo.service.AiAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiAssistantService aiAssistantService;

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> request) {
        String question = request.getOrDefault("question", "");
        String answer = aiAssistantService.ask(question);
        return Map.of("answer", answer);
    }
}
