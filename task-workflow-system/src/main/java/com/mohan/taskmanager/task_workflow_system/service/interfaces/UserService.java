package com.mohan.taskmanager.task_workflow_system.service.interfaces;


import com.mohan.taskmanager.task_workflow_system.dto.request.UserRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.UserResponseDTO;
import com.mohan.taskmanager.task_workflow_system.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // creating a user
    UserResponseDTO createUser(UserRequestDTO dto);

    // to get all the users
    List<UserResponseDTO> getAllUsers();

    // get user by id
    User getUserById(UUID userId);



}
