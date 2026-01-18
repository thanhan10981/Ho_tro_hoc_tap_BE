package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "linh_vuc")
@Getter
@Setter
public class LinhVuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_linh_vuc")
    private Integer id;

    @Column(name = "ten_linh_vuc", nullable = false)
    private String tenLinhVuc;
}
