package com.hoctap.learningsupportapi.service;

import com.hoctap.learningsupportapi.model.dto.summary.AuditLogDTO;
import com.hoctap.learningsupportapi.model.entity.AuditLog;
import com.hoctap.learningsupportapi.repository.AuditLogRepository;
import com.hoctap.learningsupportapi.utils.summary.TimeAgoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final CurrentUserService currentUserService;

    public void log(Integer userId, String moTa, String trangThai) {
        auditLogRepository.save(
                AuditLog.builder()
                        .maNguoiDung(userId)
                        .moTa(moTa)
                        .trangThai(trangThai)
                        .build()
        );
    }
    public List<AuditLogDTO> getRecentSummarySuccessLogs() {

        Integer userId = currentUserService.getCurrentUserId();

        List<AuditLog> logs = auditLogRepository
                .findTop5ByMaNguoiDungAndTrangThaiAndMoTaContainingIgnoreCaseOrderByNgayTaoDesc(
                        userId,
                        "thanh_cong",
                        "tóm tắt"
                );


        return logs.stream()
                .map(log -> AuditLogDTO.builder()
                        .moTa(log.getMoTa())
                        .thoiGian(TimeAgoUtil.format(log.getNgayTao()))
                        .build()
                )
                .collect(Collectors.toList());
    }

}
