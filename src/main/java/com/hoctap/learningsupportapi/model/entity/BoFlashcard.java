package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "bo_flashcard")
@Data
public class BoFlashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_bo_flashcard")
    private Integer maBoFlashcard;

    @Column(name = "ma_mon_hoc", nullable = false)
    private Integer maMonHoc;

    @Column(name = "ma_nguoi_dung", nullable = false)
    private Integer maNguoiDung;

    @Column(name = "ten_bo", nullable = false)
    private String tenBo;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    public void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}
