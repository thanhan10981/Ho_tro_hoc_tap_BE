package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TodayEventDTO {

    private Integer maSuKien;
    private String loaiSuKien;

    private String tieuDe;
    private String tenMonHoc;

    // dùng cho hoc
    private String thoiGianHoc; // "HH:mm - HH:mm"
    private String diaDiem;

    // dùng cho deadline / thi / on_tap
    private LocalDateTime thoiGian;

    private String moTa;
}
