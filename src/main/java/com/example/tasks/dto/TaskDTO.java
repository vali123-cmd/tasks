package com.example.tasks.dto;


import jakarta.persistence.ManyToOne;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class TaskDTO {

    private Long id;

    @NotBlank(message = "Content can't be blank")
    private String content;
    @NotNull
    private LocalDateTime dueDate;
    @NotNull
    private String statusName;

    private Long AssignedTo;


    private String createdBy;

    private LocalDateTime creationDate;

}
//test comment for versioning