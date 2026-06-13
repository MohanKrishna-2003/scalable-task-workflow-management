package com.mohan.taskmanager.task_workflow_system.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        long accessExpiration,
        long refreshExpiration
) {
}
