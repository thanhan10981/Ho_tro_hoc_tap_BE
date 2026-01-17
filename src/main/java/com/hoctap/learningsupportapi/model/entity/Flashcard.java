package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "flashcard")
@Data
@Getter
@Setter
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_flashcard")
    private Integer maFlashcard;

    @Column(name = "ma_bo_flashcard", nullable = false)
    private Integer maBoFlashcard;

    @Column(name = "ma_mon_hoc", nullable = false)
    private Integer maMonHoc;

    @Column(name = "ma_nguoi_dung", nullable = false)
    private Integer maNguoiDung;

    @Column(name = "mat_truoc", nullable = false)
    private String matTruoc;

    @Column(name = "mat_sau", nullable = false)
    private String matSau;
}
