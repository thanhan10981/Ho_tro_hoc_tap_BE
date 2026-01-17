package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AIResponse {
    private List<AIQuestion> questions;
}
