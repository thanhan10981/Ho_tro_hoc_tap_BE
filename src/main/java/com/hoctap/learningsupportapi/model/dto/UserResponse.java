package com.hoctap.learningsupportapi.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String hoTen;
    private String email;
    private String linhVucQuanTam;
    private LocalDateTime ngayTao;
}
