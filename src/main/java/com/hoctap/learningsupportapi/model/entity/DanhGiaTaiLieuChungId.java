package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DanhGiaTaiLieuChungId implements Serializable {

    @Column(name = "ma_tai_lieu")
    private Integer taiLieuId;

    @Column(name = "ma_nguoi_dung")
    private Integer nguoiDungId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DanhGiaTaiLieuChungId that)) return false;
        return Objects.equals(taiLieuId, that.taiLieuId)
                && Objects.equals(nguoiDungId, that.nguoiDungId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taiLieuId, nguoiDungId);
    }
}
