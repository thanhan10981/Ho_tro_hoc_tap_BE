package com.hoctap.learningsupportapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class AIItemDTO {
    private Integer id;
    private String title;
    private String date;
    private String time;
    private String location;
}
