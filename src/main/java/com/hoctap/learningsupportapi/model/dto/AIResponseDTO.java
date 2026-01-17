package com.hoctap.learningsupportapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIResponseDTO {
    private String greeting;
    private String summary;
    private List<AIItemDTO> items;
    private List<String> actions;
}
