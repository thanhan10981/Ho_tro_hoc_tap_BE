package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PersonalDocResponse {

    private Long personalId;
    private Integer docId;
    private Integer nhanId;

    private String title;
    private String subject;
    private String type;
    private String status;
}
