package com.mohan.taskmanager.task_workflow_system.service.impl;

import io.github.resilience4j.retry.annotation.Retry;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Retry(
            name = "notificationService",
            fallbackMethod = "sendNotificationFallback"
    )
    public void sendTaskAssignedNotification(String name, String email, String taskTitle, String body) throws MessagingException {

        log.info("Sending email to user={} mail={} for task={}", name, email, taskTitle);

        emailService.sendTaskAssignedEmail(email, taskTitle, body);

        log.info("Email sent successfully to {}", email);
    }
    // fallback MUST match method signature + Exception at end
    public void sendNotificationFallback(
            String name,
            String email,
            String taskTitle,
            String body,
            Throwable ex
    ) {
        log.error(
                "FALLBACK triggered for user={} email={} task={} reason={}",
                name,
                email,
                taskTitle,
                ex.getMessage()
        );
    }
}
