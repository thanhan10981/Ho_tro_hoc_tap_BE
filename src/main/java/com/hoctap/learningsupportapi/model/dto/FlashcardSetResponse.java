package com.hoctap.learningsupportapi.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardSetResponse {
    private Integer maBoFlashcard;
    private String tenBo;
    private String moTa;
    private Integer soLuongFlashcard;
}
