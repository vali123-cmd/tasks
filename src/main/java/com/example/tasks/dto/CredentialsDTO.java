package com.example.tasks.dto;



import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CredentialsDTO {

    private String email;
    private String password;

}
