package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class KnowledgeDocResponse {

    private Integer id;
    private String title;
    private String description;

    private String subject;   // CNTT, Toán...
    private String type;      // PDF, VIDEO
    private String nhan;      // tag

    private long size;
    private long views;
    private long downloads;

    private LocalDateTime createdAt;
}

