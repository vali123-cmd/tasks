package com.example.tasks.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "USER_ID")
    private Long userId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "BIRTH_DATE")
    private LocalDateTime birthDate;

    @Convert(converter = NumericBooleanConverter.class)
    //aveam eroare pe asta din cauza ca in bd is_internal e declarat ca numar
    @Column(name = "IS_INTERNAL")
    private boolean isInternal;

    @Column(name = "CREATION_DATE")
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "LAST_UPDATE_DATE")
    private LocalDateTime lastUpdateDate;

    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdateBy;

    @Column(name = "CREATED_BY_FULLNAME")
    private String createdByFullname;



}
