package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.MonHocCaNhan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonHocCaNhanRepository extends JpaRepository<MonHocCaNhan, Integer> {

    List<MonHocCaNhan> findByMaNguoiDung(Integer maNguoiDung);

    boolean existsByMaNguoiDungAndTenMonHoc(Integer maNguoiDung, String tenMonHoc);
}
