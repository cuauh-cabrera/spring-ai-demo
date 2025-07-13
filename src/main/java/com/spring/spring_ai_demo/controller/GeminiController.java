package com.spring.spring_ai_demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.spring.spring_ai_demo.service.IGeminiTextExtractService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api/v1/gemini")
public class GeminiController {

    private final IGeminiTextExtractService geminiTextExtractService;

    public GeminiController(IGeminiTextExtractService geminiTextExtractService) {
        this.geminiTextExtractService = geminiTextExtractService;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> askChat(@RequestBody Map<String, String> question) {
        return ResponseEntity.ok(geminiTextExtractService.textChat(question.get("prompt")));
    }

    @PostMapping(value = "/extract-document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonNode> extractDocument(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(geminiTextExtractService.processDocument(file));
    }
}
