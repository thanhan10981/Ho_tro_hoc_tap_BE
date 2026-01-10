package com.hoctap.learningsupportapi.repository;
import com.hoctap.learningsupportapi.model.entity.TaiLieuNhan;
import com.hoctap.learningsupportapi.model.entity.TaiLieuNhanId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface TaiLieuNhanRepository
        extends JpaRepository<TaiLieuNhan, TaiLieuNhanId> {

    List<TaiLieuNhan> findByNguoiDung_Id(Integer  userId);

    boolean existsByNguoiDung_IdAndTaiLieu_Id(Integer  userId, Integer  taiLieuId);

    void deleteByNguoiDung_IdAndTaiLieu_Id(Integer  userId, Integer  taiLieuId);

    List<TaiLieuNhan> findByNhan_Id(Integer nhanId);
}
