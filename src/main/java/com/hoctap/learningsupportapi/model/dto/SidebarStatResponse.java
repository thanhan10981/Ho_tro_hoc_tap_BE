package com.hoctap.learningsupportapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SidebarStatResponse {
    private Integer id;
    private String name;
    private Long count;
}
