package com.mohan.taskmanager.task_workflow_system.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * @param taskId why these fields? when this event reaches listeners they need information about the task and the user to whom it is assigned to perform necessary actions like sending notifications, updating task status etc. (Instead of querying DB again, we carry the important data.)
 */

public record TaskAssignedEvent(UUID taskId, String assignedUserName, String mail, String taskTitle, String body) {

}
