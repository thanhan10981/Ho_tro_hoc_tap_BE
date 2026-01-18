package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;

@Data
public class NoteRequest {
    private Integer docId;
    private Integer userId;
    private String content;
}
