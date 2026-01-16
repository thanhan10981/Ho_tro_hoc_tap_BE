package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tom_tat_bai_hoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TomTatBaiHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tom_tat")
    private Integer maTomTat;

    @Column(name = "ma_tai_lieu")
    private Integer maTaiLieu;

    @Column(name = "ma_nguoi_dung")
    private Integer maNguoiDung;

    @Column(name = "ma_mon_hoc")
    private Integer maMonHoc;

    @Column(name = "tieu_de")
    private String tieuDe;

    @Column(name = "noi_dung_tom_tat", columnDefinition = "NVARCHAR(MAX)")
    private String noiDungTomTat;

    @Column(name = "so_tu")
    private Integer soTu;

    @Column(name = "so_trang")
    private Integer soTrang;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}
