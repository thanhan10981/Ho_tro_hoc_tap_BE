package com.hoctap.learningsupportapi.repository;

import com.hoctap.learningsupportapi.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
}
