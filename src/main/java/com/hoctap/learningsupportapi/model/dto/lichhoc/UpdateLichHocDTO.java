package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLichHocDTO {

    private String tieuDe;
    private String diaDiem;
    private String loaiSuKien;

    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;

    private String mucDoUuTien;
    private String moTa;
}
