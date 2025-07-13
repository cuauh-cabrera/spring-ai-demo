package com.spring.spring_ai_demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;

public interface IGeminiTextExtractService {

    String textChat(String prompt);

    JsonNode processDocument(MultipartFile file);


}
