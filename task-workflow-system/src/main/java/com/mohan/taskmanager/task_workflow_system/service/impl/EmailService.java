package com.mohan.taskmanager.task_workflow_system.service.impl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTaskAssignedEmail(String to, String taskTitle, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Task Assigned: " + taskTitle);

        String htmlContent = """
                    <html>
                        <body>
                            <h2 style="color:#2E86C1;">New Task Assigned</h2>
                
                            <p>Hello,</p>
                
                            <p>You have been assigned a new task:</p>
                
                            <h3>%s</h3>
                
                            <p>%s</p>
                
                            <hr>
                
                            <p style="font-size:12px;color:gray;">
                                Please complete it before the deadline.
                            </p>
                        </body>
                    </html>
                """.formatted(taskTitle, body);

        helper.setText(htmlContent, true); // 👈 true = HTML enabled

        mailSender.send(message);

        log.info("HTML email successfully sent to {}", to);
    }
}
