package com.mohan.taskmanager.task_workflow_system.specification;

import com.mohan.taskmanager.task_workflow_system.enums.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasUserId(String userId) {
        return(root, query, cb) ->
             cb.equal(root.join("assignedUser").get("userId"), userId);
    }

    public static Specification<Task> hasStatus(TaskStatus taskStatus){
        return (root, query, cb) ->
            cb.equal(root.get("status"), taskStatus);
    }

    public static Specification<Task> notArchived(){
        return (root, query, cb) ->
                cb.equal(root.get("archived"), false);
    }

    public static Specification<Task> build(String userId, TaskStatus taskStatus){

        // generally we will call, SELECT * FROM tasks as our first step, and then we dynamically add the filters that are needed.
        Specification<Task> specification = Specification.allOf(notArchived());

        if(userId != null){
            specification = specification.and(hasUserId(userId));
        }

        if(taskStatus != null){
            specification = specification.and(hasStatus(taskStatus));
        }

        return specification;
    }


}
