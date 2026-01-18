package com.hoctap.learningsupportapi.model.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class KnowledgeDocDetailResponse {

    private Integer id;
    private String title;
    private String description;
    private String filePath;
    private String type;
    private Long size;

    private Integer views;
    private Integer downloads;
    private LocalDateTime createdAt;

    private Integer capBacId;
    private String capBacName;

    private Integer linhVucId;
    private String linhVucName;

    private Integer chuDeId;
    private String chuDeName;

    private Double avgRating;
    private Integer totalRating;
}
