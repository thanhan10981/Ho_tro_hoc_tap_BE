package com.hoctap.learningsupportapi.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mon_hoc_ca_nhan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonHocCaNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_mon_hoc")
    private Integer maMonHoc;

    @Column(name = "ma_nguoi_dung", nullable = false)
    private Integer maNguoiDung;

    @Column(name = "ten_mon_hoc", nullable = false)
    private String tenMonHoc;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "muc_do_hoc")
    private String mucDoHoc;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}
