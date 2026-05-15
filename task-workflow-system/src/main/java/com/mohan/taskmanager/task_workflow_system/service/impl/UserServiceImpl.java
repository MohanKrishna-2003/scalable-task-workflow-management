package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.exception.UserNotFoundException;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private Map<String, User> userStore = new HashMap<>();

    @Override
    public User createUser(String userId, String name, String email){
        if(userId == null || userId.isBlank()){
            throw  new UserNotFoundException("UserId cannot be empty");
        }
        User user = new User(userId, name, email);
        userStore.put(userId, user);
        return user;
    }

    @Override
    public List<User> getAllUsers(){
        return new ArrayList<>(userStore.values());
    }

    @Override
    public User getUserById(String userId){
        User user = userStore.get(userId);
        if(user == null){
            throw new UserNotFoundException("User not found with id " + userId);
        }
        return user;
    }


}
