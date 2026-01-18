package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class KnowledgeDocResponse {

    private Integer id;
    private String title;
    private String description;
    private String linhVuc;
    private String subject;
    private String type;
    private String filePath;
    private Double rating;
    private long size;
    private long views;
    private long downloads;

    private LocalDateTime createdAt;
}

