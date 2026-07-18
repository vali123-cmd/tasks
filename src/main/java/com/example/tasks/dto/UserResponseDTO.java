package com.example.tasks.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class UserResponseDTO {
    public Long id;

    private String username;

    private String email;

}
