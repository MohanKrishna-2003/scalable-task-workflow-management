package com.mohan.taskmanager.task_workflow_system.listeners;

import com.mohan.taskmanager.task_workflow_system.events.TaskAssignedEvent;
import com.mohan.taskmanager.task_workflow_system.service.impl.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;


    @Async
    @EventListener
    public void handleTaskAssigned(TaskAssignedEvent event) throws MessagingException {

        log.info("Notification listener triggered for task={}", event.taskId());

        notificationService.sendTaskAssignedNotification(
                event.assignedUserName(),
                event.mail(),
                event.taskTitle(),
                event.body()
        );
    }
}
