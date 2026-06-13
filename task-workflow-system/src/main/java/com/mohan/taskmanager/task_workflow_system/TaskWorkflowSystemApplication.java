package com.mohan.taskmanager.task_workflow_system;

import com.mohan.taskmanager.task_workflow_system.config.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableConfigurationProperties(JwtProperties.class)
public class TaskWorkflowSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskWorkflowSystemApplication.class, args);
	}

}
