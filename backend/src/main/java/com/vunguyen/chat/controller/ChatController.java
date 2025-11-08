package com.vunguyen.chat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // cho phép React gọi
@RequestMapping("/api/chat")
public class ChatController {

    private final WebClient webClient;

    @Value("${openrouter.url}")
    private String apiUrl;

    @Value("${openrouter.model}")
    private String model;

    public ChatController(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostMapping
    public Map<String, Object> chat(@RequestBody Map<String, String> body) {
        String message = body.get("message");

        // payload gửi OpenRouter
        Map<String, Object> payload = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", message)
                )
        );

        // Gọi API OpenRouter
        Map<String, Object> response = webClient.post()
                .uri(apiUrl)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response; // Trả nguyên JSON cho frontend
    }

    @GetMapping("/test")
    public String testApi() {
        return "Backend is running!";
    }
}
