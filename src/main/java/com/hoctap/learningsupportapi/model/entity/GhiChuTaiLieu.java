package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import java.time.LocalDateTime;


@Entity
@Table(name = "ghi_chu_tai_lieu")
@Getter
@Setter
public class GhiChuTaiLieu {

    @Id
    @GeneratedValue
    @Column(name = "ma_ghi_chu", columnDefinition = "uniqueidentifier")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ma_tai_lieu")
    private TaiLieuChung taiLieu;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung nguoiDung;

    @Column(name = "noi_dung", columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Column(name = "ngay_tao")
    private LocalDateTime createdAt;

    @Column(name = "canvas_json", columnDefinition = "NVARCHAR(MAX)")
    private String canvasJson;

//    public void setTaiLieu(TaiLieuChung referenceById) {
//    }
//
//    public void setNguoiDung(NguoiDung nd) {
//    }
}
