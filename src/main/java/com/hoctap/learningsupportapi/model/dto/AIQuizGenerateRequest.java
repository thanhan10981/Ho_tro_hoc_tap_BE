package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;

@Data
public class AIQuizGenerateRequest {
    private Integer maQuiz;
    private String topic;
    private Integer numQuestions;
    private String difficulty;
}
