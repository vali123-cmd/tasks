package com.example.tasks.service;

import com.example.tasks.domain.User;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.mapper.UserMapper;
import com.example.tasks.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers(){
        log.info("Users retrieved");
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }
    @Transactional
    public UserDTO createUser(UserDTO userDTO){
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDTO)));
    }

    public UserDTO getUserById(Long id){
        return userRepository.findById(id).map(userMapper::toDto).orElse(null);
    }

    @Transactional
    public void deleteUserById(Long id){
        userRepository.deleteById(id);
        log.info("User with id: {} deleted", id);

    }
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO){
        User targetUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        targetUser.setBirthDate(userDTO.getBirthDate());
        targetUser.setUsername(userDTO.getUsername());
        targetUser.setInternal(userDTO.isInternal());
        targetUser.setLastUpdateDate(LocalDateTime.now());

        User updatedUser = userRepository.save(targetUser);

        return userMapper.toDto(updatedUser);
    }

    public UserDTO getUserByUsername(String username){
        return userRepository.findByUsername(username).map(userMapper::toDto).orElse(null);
    }







}
