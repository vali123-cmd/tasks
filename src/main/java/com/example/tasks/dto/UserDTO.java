package com.example.tasks.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {

    private Long userId;
    @NotBlank(message = "Username can't be blank")
    private String username;
    @NotNull
    private LocalDateTime birthDate;

    @NotBlank(message = "Password can't be blank")
    private String password;

    @NotBlank(message = "Email can't be blank")
    private String email;


    private boolean isInternal;
    private String createdBy;
    private LocalDateTime creationDate;




}
