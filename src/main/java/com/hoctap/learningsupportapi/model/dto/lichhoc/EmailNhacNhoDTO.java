package com.hoctap.learningsupportapi.model.dto.lichhoc;

import lombok.*;

import java.time.LocalDateTime;
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EmailNhacNhoDTO {

    private Integer nhacId;
    private String tieuDe;
    private Integer maSuKien;
    private String hoTen;
    private String email;

    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;

    private String tenMonHoc;
    private String loaiSuKien;
    private String mucDoUuTien;
    private String diaDiem;
    private String moTa;

    /**
     * 0 = nhắc trước bắt đầu
     * 1 = nhắc trước kết thúc
     */
    private Boolean loaiNhacNho;
}
