package com.example.tasks.dto;


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
    @NotNull
    private Long id;

    @NotBlank(message = "Content can't be blank")
    private String content;
    @NotNull
    private LocalDateTime dueDate;
    @NotBlank
    private String status;
}
//test comment for versioning