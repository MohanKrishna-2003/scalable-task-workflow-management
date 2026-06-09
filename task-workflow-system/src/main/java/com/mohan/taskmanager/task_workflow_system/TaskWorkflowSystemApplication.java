package com.mohan.taskmanager.task_workflow_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TaskWorkflowSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskWorkflowSystemApplication.class, args);
	}

}
