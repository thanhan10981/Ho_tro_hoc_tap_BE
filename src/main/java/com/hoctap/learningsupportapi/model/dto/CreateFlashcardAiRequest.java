package com.hoctap.learningsupportapi.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFlashcardAiRequest {
    private Integer maBoFlashcard;
    private String content;
    private Integer amount;
}
