package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;

@Data
public class KnowledgeSearchRequest {

    private String keyword;
    private String type;
    private String subject;
    private Integer rating;

    private int page = 0;
    private int size = 6;
    private String sort = "createdAt";
}
