package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "cau_hoi_quiz")
@Getter
@Setter
public class CauHoiQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_cau_hoi")
    private Integer maCauHoi;

    @Column(name = "ma_quiz")
    private Integer maQuiz;

    @Column(name = "noi_dung_cau_hoi")
    private String noiDung;

    @OneToMany(mappedBy = "cauHoi", fetch = FetchType.LAZY)
    private List<DapAnQuiz> dapAn;
}
