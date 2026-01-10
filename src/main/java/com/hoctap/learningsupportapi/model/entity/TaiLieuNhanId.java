package com.hoctap.learningsupportapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiLieuNhanId implements Serializable {

    @Column(name = "ma_tai_lieu")
    private Integer taiLieuId;

    @Column(name = "ma_nguoi_dung")
    private Integer nguoiDungId;

    @Column(name = "ma_nhan")
    private Integer nhanId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaiLieuNhanId that)) return false;
        return Objects.equals(taiLieuId, that.taiLieuId)
                && Objects.equals(nhanId, that.nhanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taiLieuId, nhanId);
    }
}
