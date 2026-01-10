package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "danh_gia_tai_lieu_chung")
@Getter
@Setter
public class DanhGiaTaiLieuChung {

    @EmbeddedId
    private DanhGiaTaiLieuChungId id;

    @ManyToOne
    @MapsId("taiLieuId")
    @JoinColumn(name = "ma_tai_lieu")
    private TaiLieuChung taiLieu;

    @ManyToOne
    @MapsId("nguoiDungId")
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung nguoiDung;

    @Column(name = "so_sao")
    private Integer soSao;
}
