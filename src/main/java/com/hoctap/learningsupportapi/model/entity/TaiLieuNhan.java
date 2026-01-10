package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tai_lieu_nhan")
@Getter @Setter
public class TaiLieuNhan {

    @EmbeddedId
    private TaiLieuNhanId id;

    @ManyToOne
    @MapsId("taiLieuId")
    @JoinColumn(name = "ma_tai_lieu")
    private TaiLieuChung taiLieu;

    @ManyToOne
    @MapsId("nhanId")
    @JoinColumn(name = "ma_nhan")
    private Nhan nhan;

    @ManyToOne
    @MapsId("nguoiDungId")
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung nguoiDung;

    @Column(name = "trang_thai")
    private String status;

    @Column(name = "ngay_luu")
    private LocalDateTime savedAt;

}
