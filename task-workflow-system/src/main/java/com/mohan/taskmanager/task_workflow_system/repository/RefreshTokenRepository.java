package com.mohan.taskmanager.task_workflow_system.repository;

import com.mohan.taskmanager.task_workflow_system.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository
                    extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);
}
