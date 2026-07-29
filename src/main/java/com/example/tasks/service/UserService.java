package com.example.tasks.service;

import com.example.tasks.domain.Role;
import com.example.tasks.domain.User;
import com.example.tasks.dto.CredentialsDTO;
import com.example.tasks.dto.UserDTO;

import com.example.tasks.dto.UserResponseDTO;
import com.example.tasks.mapper.UserMapper;
import com.example.tasks.repository.RoleRepository;
import com.example.tasks.repository.UserRepository;
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
    private final RoleRepository roleRepository;

    public List<UserDTO> getAllUsers(){
        log.info("Users retrieved");
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }
    @Transactional
    public UserDTO createUser(UserDTO userDTO){
        String rolename = userDTO.getRoleName();
        if(rolename == null){
            rolename = "USER";
        }
        Role role = roleRepository.findByRolename(rolename).orElseThrow(() -> new RuntimeException("Role not found"));
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDTO, role)));
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
        String rolename = userDTO.getRoleName();
        if (rolename != null) {
            Role role = roleRepository.findByRolename(rolename).orElseThrow(() -> new RuntimeException("Role not found"));
            targetUser.setRole(role);
        }

        targetUser.setBirthDate(userDTO.getBirthDate());
        targetUser.setUsername(userDTO.getUsername());
        targetUser.setEmail(userDTO.getEmail());
        targetUser.setPassword(userDTO.getPassword());
        targetUser.setInternal(userDTO.isInternal());
        targetUser.setLastUpdateDate(LocalDateTime.now());

        User updatedUser = userRepository.save(targetUser);

        return userMapper.toDto(updatedUser);
    }

    public UserDTO getUserByUsername(String username){
        return userRepository.findByUsername(username).map(userMapper::toDto).orElse(null);
    }

    public UserResponseDTO login(CredentialsDTO userLoginDTO){
        User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPassword().equals(userLoginDTO.getPassword())){
            return UserResponseDTO.builder()
                    .id(user.getUserId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        } else {
            throw new RuntimeException("Wrong password");
        }

    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }


}
