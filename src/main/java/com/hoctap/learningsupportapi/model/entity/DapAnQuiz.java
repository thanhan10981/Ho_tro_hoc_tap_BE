package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dap_an_quiz")
@Getter
@Setter
public class DapAnQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_dap_an")
    private Integer maDapAn;

    @Column(name = "noi_dung")
    private String noiDung;

    @Column(name = "is_dung")
    private Boolean isDung;

    @ManyToOne
    @JoinColumn(name = "ma_cau_hoi")
    private CauHoiQuiz cauHoi;
}
