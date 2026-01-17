package com.hoctap.learningsupportapi.controller.summary;


import com.hoctap.learningsupportapi.model.dto.summary.AuditLogDTO;

import com.hoctap.learningsupportapi.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-log")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/recent")
    public List<AuditLogDTO> getRecentLogs() {
        return auditLogService.getRecentSummarySuccessLogs();
    }
}
