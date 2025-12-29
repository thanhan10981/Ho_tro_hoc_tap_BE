package com.hoctap.learningsupportapi.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponse {

    private String hoTen;
    private String email;
    private Integer capBacHoc;
    private String linhVucQuanTam;
    private LocalDateTime ngayTao;
}
