package com.hoctap.learningsupportapi.model.dto;

import lombok.Data;

@Data
public class SaveNhanRequest {
    private Integer userId;
    private Integer docId;
    private Integer nhanId;
}
