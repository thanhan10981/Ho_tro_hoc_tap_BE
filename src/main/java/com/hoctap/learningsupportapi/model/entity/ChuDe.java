package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chu_de")
@Getter @Setter
public class ChuDe {

    @Id
    @Column(name = "ma_chu_de")
    private Integer id;

    @Column(name = "ten_chu_de")
    private String tenChuDe;

    @ManyToOne
    @JoinColumn(name = "ma_linh_vuc")
    private LinhVuc linhVuc;
}
