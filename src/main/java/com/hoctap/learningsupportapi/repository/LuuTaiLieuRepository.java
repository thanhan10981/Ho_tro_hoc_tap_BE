package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.LuuTaiLieu;
import com.hoctap.learningsupportapi.model.entity.LuuTaiLieuId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LuuTaiLieuRepository
        extends JpaRepository<LuuTaiLieu, LuuTaiLieuId> {

    // kiểm tra đã lưu chưa
    boolean existsByNguoiDung_IdAndTaiLieu_Id(Integer userId, Integer docId);

    // lấy toàn bộ kho cá nhân của user
    List<LuuTaiLieu> findByNguoiDung_Id(Integer userId);

    // xóa 1 tài liệu khỏi kho cá nhân
    void deleteByNguoiDung_IdAndTaiLieu_Id(Integer userId, Integer docId);
    Optional<LuuTaiLieu> findByNguoiDung_IdAndTaiLieu_Id(
            Integer nguoiDungId,
            Integer taiLieuId
    );

}
