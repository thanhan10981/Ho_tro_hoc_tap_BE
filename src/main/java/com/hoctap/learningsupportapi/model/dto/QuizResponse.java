package com.hoctap.learningsupportapi.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizResponse {
    private Integer maQuiz;
    private String tenQuiz;
    private String moTa;
    private Integer maMonHoc;
}
