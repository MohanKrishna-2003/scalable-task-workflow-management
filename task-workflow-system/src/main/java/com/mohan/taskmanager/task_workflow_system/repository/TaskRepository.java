package com.mohan.taskmanager.task_workflow_system.repository;

import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface TaskRepository extends
        JpaRepository<Task, UUID>,
        JpaSpecificationExecutor<Task> {

    // here we dont need these custom methods becuase we have done dymamic filtering using specification. but if we want to do it using custom methods then we can use these methods. but it will be a bit redundant because we have already done it using specification.
//    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
//
//    Page<Task> findByAssignedUser_UserId(String userId, Pageable pageable);
//
//    Page<Task> findByAssignedUser_UserIdAndStatus(String userId, TaskStatus status, Pageable pageable);


}
