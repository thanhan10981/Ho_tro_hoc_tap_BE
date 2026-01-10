package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nhan")
public class Nhan {

    @Id
    @Column(name = "ma_nhan")
    private Integer id;

    @Column(name = "ten_nhan")
    private String tenNhan;
}
