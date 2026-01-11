package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lich_hoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_su_kien")
    private Integer maSuKien;

    @Column(name = "ma_nguoi_dung")
    private Integer maNguoiDung;

    @Column(name = "ma_mon_hoc")
    private Integer maMonHoc;

    @Column(name = "tieu_de")
    private String tieuDe;

    @Column(name = "loai_su_kien")
    private String loaiSuKien;

    @Column(name = "thoi_gian_bat_dau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "muc_do_uu_tien")
    private String mucDoUuTien;

    @Column(name = "dia_diem")
    private String diaDiem;

    @Column(name = "mo_ta")
    private String moTa;
}
