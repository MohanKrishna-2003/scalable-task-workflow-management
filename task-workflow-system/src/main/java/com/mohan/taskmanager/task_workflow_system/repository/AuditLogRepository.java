package com.mohan.taskmanager.task_workflow_system.repository;

import com.mohan.taskmanager.task_workflow_system.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

}
