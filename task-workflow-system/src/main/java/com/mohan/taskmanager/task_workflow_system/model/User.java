package com.mohan.taskmanager.task_workflow_system.model;

import com.mohan.taskmanager.task_workflow_system.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    // name = username
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active;

    private LocalDateTime createdAt;

}
