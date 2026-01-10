package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoteResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
}
