package com.mohan.taskmanager.task_workflow_system.listeners;

import com.mohan.taskmanager.task_workflow_system.events.TaskAssignedEvent;
import com.mohan.taskmanager.task_workflow_system.model.AuditLog;
import com.mohan.taskmanager.task_workflow_system.repository.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AuditLogListener {

    private final AuditLogRepository auditLogRepository;

    public AuditLogListener(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }


    @Async
    @EventListener
    public void handleTaskAssigned(TaskAssignedEvent event) throws Exception {

        log.info(
                "Started audit processing for task={}",
                event.taskId()
        );
        Thread.sleep(5000); // Simulate delay for audit log processing

        AuditLog auditLog = new AuditLog();
        auditLog.setAction("TASK_ASSIGNED");

        auditLog.setEntityType("TASK");

        auditLog.setEntityId(
                event.taskId().toString()
        );

        auditLog.setPerformedBy(
                event.assignedUserName()
        );

        auditLog.setCreatedAt(
                LocalDateTime.now()
        );

        auditLogRepository.save(auditLog);
        log.info(
                "Audit record persisted for task={}",
                event.taskId()
        );

        log.info(
                "AUDIT LOG -> Task [{}] assigned to User [{}]",
                event.taskTitle(),
                event.assignedUserName()
        );


    }
}