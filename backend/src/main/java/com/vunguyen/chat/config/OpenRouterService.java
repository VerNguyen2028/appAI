package com.vunguyen.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Service
public class OpenRouterService {

    @Value("${openrouter.url}")
    private String openRouterUrl;

    @Value("${openrouter.apikey}")
    private String openRouterApiKey;

    @Value("${openrouter.model}")
    private String openRouterModel;

    public String getChatCompletion(String prompt) {
        WebClient client = WebClient.builder()
                .baseUrl(openRouterUrl)
                .defaultHeader("Authorization", "Bearer " + openRouterApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

        return client.post()
                .bodyValue(Map.of(
                        "model", openRouterModel,
                        "messages", List.of(
                                Map.of("role", "user", "content", prompt)
                        )
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
