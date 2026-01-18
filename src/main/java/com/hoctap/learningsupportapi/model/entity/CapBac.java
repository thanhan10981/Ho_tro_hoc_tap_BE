package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cap_bac")
@Getter @Setter
public class CapBac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_cap_bac")
    private Integer id;

    @Column(name = "ten_cap_bac")
    private String tenCapBac;
}
