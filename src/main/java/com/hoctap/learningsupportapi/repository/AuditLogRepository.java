package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    List<AuditLog> findTop5ByMaNguoiDungAndTrangThaiAndMoTaContainingIgnoreCaseOrderByNgayTaoDesc(
            Integer maNguoiDung,
            String trangThai,
            String keyword
    );

}
