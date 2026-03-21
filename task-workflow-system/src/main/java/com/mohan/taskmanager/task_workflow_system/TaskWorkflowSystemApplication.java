package com.mohan.taskmanager.task_workflow_system;

import com.mohan.taskmanager.task_workflow_system.model.Priority;
import com.mohan.taskmanager.task_workflow_system.model.Task;
import com.mohan.taskmanager.task_workflow_system.model.TaskStatus;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.service.TaskService;
import com.mohan.taskmanager.task_workflow_system.service.TaskServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskWorkflowSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskWorkflowSystemApplication.class, args);
		TaskService taskService = new TaskServiceImpl();

		// creating users
		User user1 = new User("u1", "Luffy", "luffy@gmail.com");
		User user2 = new User("u2", "Zoro", "zoro@gmail.com");

		// assigning tasks to users
		// actually we are typecasting to TaskServiceImpl to access the user store, which is not a good practice. In real application we should have a separate UserService to manage users.
		((TaskServiceImpl) taskService).getUserStore().put("u1", user1);
		((TaskServiceImpl) taskService).getUserStore().put("u2", user2);

		// creating tasks
		Task task1 = taskService.createTask("Design Database", "Design the database schema for the project", Priority.HIGH);
		Task task2 = taskService.createTask("Implement API", "Develop the RESTful API for task management", Priority.MEDIUM);

		// assigning tasks to users
		taskService.assignTask(task1.getTaskId(), "u1");
		taskService.assignTask(task2.getTaskId(), "u2");

		// updating task status
		taskService.updateStatus(task1.getTaskId(), TaskStatus.IN_PROGRESS);

		// fetching tasks by user
		System.out.println("Tasks assigned to u1:");
		taskService.getTaskByUser("u1").forEach(task -> {
			System.out.println(task.getTitle()
			+ " - " + task.getStatus() + " - " + task.getAssignedUser().getName());
		});

	}

}
