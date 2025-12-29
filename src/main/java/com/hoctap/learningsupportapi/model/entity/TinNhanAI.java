package com.hoctap.learningsupportapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tin_nhan_ai")
@Getter
@Setter
public class TinNhanAI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tin_nhan")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ma_cuoc_tro_chuyen")
    @JsonIgnore
    private CuocTroChuyenAI conversation;

    @Column(name = "nguoi_gui")
    private String sender; // "nguoi_dung" | "ai"

    @Column(name = "noi_dung")
    private String content;

    @Column(name = "thoi_gian_gui")
    private LocalDateTime createdAt;
}
