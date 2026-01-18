package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LuuTaiLieuId implements Serializable {

    @Column(name = "ma_tai_lieu")
    private Integer taiLieuId;

    @Column(name = "ma_nguoi_dung")
    private Integer nguoiDungId;
}