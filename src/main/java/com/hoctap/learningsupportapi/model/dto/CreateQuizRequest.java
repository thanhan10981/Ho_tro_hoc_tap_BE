package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;

@Data
public class CreateQuizRequest {
    private Integer maMonHoc;
    private String tenQuiz;
    private String moTa;
}
