package com.mohan.taskmanager.task_workflow_system.repository;

import com.mohan.taskmanager.task_workflow_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
        User findByUserId(String userId);
}
