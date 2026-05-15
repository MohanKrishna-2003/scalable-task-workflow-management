package com.mohan.taskmanager.task_workflow_system.service.interfaces;


import com.mohan.taskmanager.task_workflow_system.model.User;

import java.util.List;

public interface UserService {

    // creating a user
    User createUser(String userId, String name, String email);

    // to get all the users
    List<User> getAllUsers();

    // get user by id
    User getUserById(String userId);



}
