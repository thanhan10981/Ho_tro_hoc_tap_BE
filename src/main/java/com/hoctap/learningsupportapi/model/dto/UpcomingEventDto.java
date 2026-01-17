package com.hoctap.learningsupportapi.model.dto;

import java.time.LocalDateTime;

public interface UpcomingEventDto {

    Integer getMaSuKien();
    String getTieuDe();
    String getMoTa();
    LocalDateTime getThoiGianBatDau();
    String getMucDoUuTien();
    String getDiaDiem();
}
