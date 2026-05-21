package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.dto.request.UserRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.UserResponseDTO;
import com.mohan.taskmanager.task_workflow_system.exception.UserNotFoundException;
import com.mohan.taskmanager.task_workflow_system.mapper.TaskMapper;
import com.mohan.taskmanager.task_workflow_system.mapper.UserMapper;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.repository.UserRepository;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserResponseDTO createUser(@RequestBody UserRequestDTO dto){
        User user = UserMapper.toEntity(dto);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole("developer");
        user.setActive(false);
        userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    public User getUserById(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        return user;
    }


}
