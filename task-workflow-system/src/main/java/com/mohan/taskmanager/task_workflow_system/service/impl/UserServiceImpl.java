package com.mohan.taskmanager.task_workflow_system.service.impl;

import com.mohan.taskmanager.task_workflow_system.dto.request.UserRequestDTO;
import com.mohan.taskmanager.task_workflow_system.dto.response.UserResponseDTO;
import com.mohan.taskmanager.task_workflow_system.enums.Role;
import com.mohan.taskmanager.task_workflow_system.exception.UserAlreadyExistsException;
import com.mohan.taskmanager.task_workflow_system.exception.UserNotFoundException;
import com.mohan.taskmanager.task_workflow_system.mapper.TaskMapper;
import com.mohan.taskmanager.task_workflow_system.mapper.UserMapper;
import com.mohan.taskmanager.task_workflow_system.model.User;
import com.mohan.taskmanager.task_workflow_system.repository.UserRepository;
import com.mohan.taskmanager.task_workflow_system.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @CacheEvict(value = "users", allEntries = true)
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto){

        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            log.warn(
                    "Registration failed. Email already exists {}",
                    dto.getEmail()
            );
            throw new UserAlreadyExistsException("User already exists with email: " + dto.getEmail());
        }

        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        user.setActive(false);
        userRepository.save(user);
        log.info(
                "User created successfully userId={} email={}",
                user.getUserId(),
                user.getEmail()
        );
        return UserMapper.toDTO(user);
    }

    @Cacheable(value = "users", key = "'all'")
    @Override
    public List<UserResponseDTO> getAllUsers(){
        log.info("DB CALLED");
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Cacheable(value = "users:byId", key = "#userId")
    @Override
    public User getUserById(UUID userId){
        log.debug(
                "Fetching user with id={}",
                userId
        );
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user;
    }


}
