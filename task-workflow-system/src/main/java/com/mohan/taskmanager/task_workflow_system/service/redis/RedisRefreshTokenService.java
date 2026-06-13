package com.mohan.taskmanager.task_workflow_system.service.redis;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisRefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    public RedisRefreshTokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String token, String userId, long expirySeconds) {
        String key = "refresh:" + token;
        redisTemplate.opsForValue().set(key, userId, expirySeconds, TimeUnit.SECONDS);
    }

    public String getUserIdByToken(String token) {
        return redisTemplate.opsForValue().get("refresh:" + token);
    }

    public void delete(String token) {
        redisTemplate.delete("refresh:" + token);
    }
}
