package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEventRequest {

    private String tieuDe;
    private String moTa;
    private Integer maMonHoc;
    private String loaiSuKien;

    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;

    private String mucDoUuTien;
    private String diaDiem;

    // ===== REMINDER NÂNG CẤP =====
    private Boolean nhacTruocBatDau;
    private Integer soPhutTruocBatDau;

    private Boolean nhacTruocKetThuc;
    private Integer soPhutTruocKetThuc;
    private String emailNhacNho;

}
