package com.spring.spring_ai_demo.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.spring.spring_ai_demo.service.IGeminiTextExtractService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Map;

@Service
public class GeminiTextExtractServiceImpl implements IGeminiTextExtractService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.extraction.prompt}")
    private String extractionPrompt;

    private final WebClient webClient;

    public GeminiTextExtractServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Override
    public String textChat(String prompt) {
        Map<String, Object> request = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)}
        )});

        return webClient.post()
                .uri(geminiApiUrl)
                .header("Content-Type", "application/json")
                .header("X-goog-api-key", geminiApiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public JsonNode processDocument(MultipartFile file) {
        try {
            Map<String, Object> request = createDocumentRequest(file);
            
            return webClient.post()
                    .uri(geminiApiUrl)
                    .header("Content-Type", "application/json")
                    .header("X-goog-api-key", geminiApiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to process document: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> createDocumentRequest(MultipartFile file) {
        String base64Content;
        try {
            base64Content = Base64.getEncoder().encodeToString(file.getBytes());
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to read file content: " + e.getMessage(), e);
        }
        String mimeType = file.getContentType();
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        return Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", extractionPrompt),
                                Map.of("inline_data", Map.of(
                                        "mimeType", mimeType,
                                        "data", base64Content
                                ))
                        })
                }
        );
    }
}
