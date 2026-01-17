package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;

@Data
public class CreateBoFlashcardRequest {
    private Integer maMonHoc;
    private String tenBo;
    private String moTa;
    private Integer soLuongFlashcard;
}
