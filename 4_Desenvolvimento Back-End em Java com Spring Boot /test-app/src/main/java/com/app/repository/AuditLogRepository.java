package com.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}