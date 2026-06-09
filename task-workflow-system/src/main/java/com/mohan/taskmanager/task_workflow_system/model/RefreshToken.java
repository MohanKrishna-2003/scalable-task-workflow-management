package com.mohan.taskmanager.task_workflow_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String id;

    private String token;

    private String userId;

    private LocalDateTime expiryDate;

    private boolean revoked;

    public RefreshToken(String token, String userId, LocalDateTime expiryDate, boolean revoked){
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
        this.revoked = revoked;
    }

}
