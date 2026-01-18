package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "luu_tai_lieu")
@Getter
@Setter
public class LuuTaiLieu {

    @EmbeddedId
    private LuuTaiLieuId id;

    @ManyToOne
    @MapsId("taiLieuId")
    @JoinColumn(name = "ma_tai_lieu")
    private TaiLieuChung taiLieu;

    @ManyToOne
    @MapsId("nguoiDungId")
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung nguoiDung;

    // ma_mon_hoc (nullable)
    @ManyToOne
    @JoinColumn(name = "ma_mon_hoc")
    private MonHocCaNhan monHoc;

    @Column(name = "ngay_luu")
    private LocalDateTime savedAt;

    @Column(name = "trang_thai", length = 20)
    private String status;
}
