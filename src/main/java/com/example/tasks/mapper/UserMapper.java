package com.example.tasks.mapper;


import com.example.tasks.domain.User;
import com.example.tasks.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class UserMapper {
    public UserDTO toDto(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .createdBy(user.getCreatedBy())
                .birthDate(user.getBirthDate())
                .isInternal(user.isInternal())
                .creationDate(user.getCreationDate())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        LocalDateTime date = userDTO.getCreationDate();
        if(userDTO.getCreationDate() == null)
        {
            date = LocalDateTime.now();
        }
        String createdBy = userDTO.getCreatedBy();
        if(createdBy == null)
            createdBy = "System";
        return User.builder().
                username(userDTO.getUsername()).
                birthDate(userDTO.getBirthDate()).
                createdBy(createdBy).
                lastUpdateDate(LocalDateTime.now()).
                lastUpdateBy(createdBy).
                creationDate(date).
                isInternal(userDTO.isInternal()).
                build();
    }
}
