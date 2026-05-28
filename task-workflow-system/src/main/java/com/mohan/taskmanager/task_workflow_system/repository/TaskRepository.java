package com.mohan.taskmanager.task_workflow_system.repository;

import com.mohan.taskmanager.task_workflow_system.dto.response.TaskResponseDTO;
import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends
        JpaRepository<Task, UUID>,
        JpaSpecificationExecutor<Task> {

    // here we don't need these custom methods becuase we have done dymamic filtering using specification. but if we want to do it using custom methods then we can use these methods. but it will be a bit redundant because we have already done it using specification.
//    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
//
//    Page<Task> findByAssignedUser_UserId(String userId, Pageable pageable);
//
//    Page<Task> findByAssignedUser_UserIdAndStatus(String userId, TaskStatus status, Pageable pageable);

//    hey here when we use join fetch we might encounter the duplication issue mainly for @OneToMany or @ManyToMany so any ways @EntityGraph is best when compared to this.
//    @Query("SELECT t fROM Task t join fetch t.assignedUser")
//    List<Task> findAllWithUsers();

//     so here with @EntityGraph eventhough we are fixing the N+1 query issue hibernate loads all the entity fields from the entities which increases the memory usaage and storing so that's why we will go to the DTO projections which is the final optimization level.
    @Override
    @EntityGraph(attributePaths = {"assignedUser"})
    Page<Task> findAll(Specification<Task> specification, Pageable pageable);

    // DTO PROJECTION:
    // but actually here we should not use for Page beucase of it combines the redundant row and gives the wrong results.
//    @Query("""
//            SELECT new com.mohan.taskmanager.task_workflow_system.dto.response.TaskResponseDTO(
//                t.taskId,
//                t.title,
//                t.description,
//                t.priority,
//                t.taskStatus,
//                t.dueDate,
//                u.assignedUserName
//            )
//            FROM Task t
//            LEFT JOIN t.assignedUser u
//            """
//            )
//    Page<TaskResponseDTO> findAllTasksDTO(Specification specification, Pageable pageable);



}
