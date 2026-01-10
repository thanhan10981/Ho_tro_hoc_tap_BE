package com.hoctap.learningsupportapi.repository;
import java.util.UUID;


import com.hoctap.learningsupportapi.model.entity.GhiChuTaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GhiChuTaiLieuRepository
        extends JpaRepository<GhiChuTaiLieu, UUID> {

    List<GhiChuTaiLieu> findByTaiLieu_IdAndNguoiDung_Id(Integer  taiLieuId, Integer nguoiDungId);

    GhiChuTaiLieu findTopByTaiLieu_IdAndNguoiDung_IdOrderByCreatedAtDesc(
            Integer  taiLieuId, Integer nguoiDungId
    );
}
