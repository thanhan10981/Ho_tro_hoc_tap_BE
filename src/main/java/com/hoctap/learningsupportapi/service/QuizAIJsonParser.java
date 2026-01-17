package com.hoctap.learningsupportapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoctap.learningsupportapi.model.dto.AIResponse;
import org.springframework.stereotype.Component;

@Component
public class QuizAIJsonParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public AIResponse parse(String json) {
        try {
            return mapper.readValue(json, AIResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("AI trả JSON không hợp lệ", e);
        }
    }
}
