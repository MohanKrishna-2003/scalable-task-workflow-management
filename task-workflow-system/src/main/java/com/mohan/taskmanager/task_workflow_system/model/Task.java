package com.mohan.taskmanager.task_workflow_system.model;


import com.mohan.taskmanager.task_workflow_system.enums.Priority;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.exception.UserNotFoundException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "tasks",
        indexes = {
                @Index(name = "idx_task_status", columnList = "status"),
                @Index(name = "idx_task_user", columnList = "user_id"),
                @Index(name = "idx_task_created", columnList = "createdAt"),
                @Index(name = "idx_user_status", columnList = "user_id, status") // this is called composite indexing

        }
    )
@Where(clause = "archived = false")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User assignedUser;

    private String createdBy;

    private boolean archived;


    // Behavior Methods (Important for LLD)
    public void assignUser(User user){
        if(user == null){
            throw new IllegalArgumentException("User cannot be null");
        }
        this.assignedUser = user;
    }

    public void updateNewStatus(TaskStatus newStatus){
        if(newStatus == null){
            throw new IllegalArgumentException("Status cannot be null");
        }
        if(status == newStatus) return;
        if(status == TaskStatus.TODO && newStatus == TaskStatus.IN_PROGRESS){
            this.status = newStatus;
        } else if (this.status == TaskStatus.IN_PROGRESS && newStatus == TaskStatus.COMPLETED) {
            this.status = newStatus;
        } else {
            throw new IllegalStateException(
                    "Invalid status transition from " + this.status + " to " + newStatus
            );
        }
    }

    public void updatePriority(Priority priority){
        if(priority == null){
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.priority = priority;
    }
}
