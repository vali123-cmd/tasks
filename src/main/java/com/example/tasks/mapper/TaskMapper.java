package com.example.tasks.mapper;

import com.example.tasks.domain.StatusType;
import com.example.tasks.domain.Task;
import com.example.tasks.dto.TaskDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .content(task.getName())
                .userId(task.getUserId())
                .statusName(task.getStatusType() != null ? task.getStatusType().getStatusName() : null)
                .dueDate(task.getDueDate())
                .creationDate(task.getCreationDate())
                .createdBy(task.getCreatedBy())
                .build();
    }

    public Task toEntity(TaskDTO taskDTO, StatusType statusType) {


        return Task.builder()
                .name(taskDTO.getContent())
                .userId(taskDTO.getUserId())
                .statusType(statusType)
                .dueDate(taskDTO.getDueDate())
                .creationDate(LocalDateTime.now())
                .createdBy(taskDTO.getCreatedBy())
                .lastUpdateDate(LocalDateTime.now())
                .lastUpdateBy(taskDTO.getCreatedBy())
                .createdByFullname(taskDTO.getCreatedBy())

                .build();
    }
}