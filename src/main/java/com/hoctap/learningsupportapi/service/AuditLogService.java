package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.entity.AuditLog;
import com.hoctap.learningsupportapi.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(Integer userId, String moTa, String trangThai) {
        auditLogRepository.save(
                AuditLog.builder()
                        .maNguoiDung(userId)
                        .moTa(moTa)
                        .trangThai(trangThai)
                        .build()
        );
    }
}
