package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tom_tat_tu_khoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TomTatTuKhoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tu_khoa")
    private Integer maTuKhoa;

    @Column(name = "ma_tom_tat")
    private Integer maTomTat;

    @Column(name = "tu_khoa")
    private String tuKhoa;
}
