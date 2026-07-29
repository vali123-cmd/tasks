package com.example.tasks.domain;

import jakarta.persistence.*;
import lombok.*;
import com.example.tasks.domain.Permission;


import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "roles")


public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "ROLE_ID")
    private Long roleId;

    @Column(name = "ROLE_NAME")
    private String rolename;

    @ManyToMany
    @JoinTable(
            name="rolespermissions",
            joinColumns = @JoinColumn(name="ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name="PERMISSION_ID")
    )
    private Set<Permission> permission;




}
