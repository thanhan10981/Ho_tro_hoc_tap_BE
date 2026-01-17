package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AIQuestion {
    private String noiDung;
    private List<AIAnswer> dapAn;
}
