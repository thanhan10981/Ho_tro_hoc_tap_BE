package com.hoctap.learningsupportapi.model.dto;
import lombok.Data;

@Data
public class CreateFlashcardRequest {
    private Integer maBoFlashcard;
    private String matTruoc;
    private String matSau;
}
