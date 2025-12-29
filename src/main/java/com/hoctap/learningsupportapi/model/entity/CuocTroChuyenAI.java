package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuoc_tro_chuyen_ai")
@Getter
@Setter
public class CuocTroChuyenAI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_cuoc_tro_chuyen")
    private Integer id;

    @Column(name = "ma_nguoi_dung")
    private Integer userId;

    @Column(name = "ma_mon_hoc")
    private Integer monHocId;

    @Column(name = "ngay_tao")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<TinNhanAI> messages = new ArrayList<>();
}
