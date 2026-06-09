package com.mohan.taskmanager.task_workflow_system.config.security;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    public static String getCurrentUserEmail(){

        return SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();
    }

}
