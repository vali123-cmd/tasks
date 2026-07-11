package com.example.tasks.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TASK_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS_TYPE_ID")
    private String statusTypeId;

    @Column(name = "USER_ID")

    private Long userId;

    @Column(name = "DUE_DATE")
    private LocalDateTime dueDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATION_DATE")
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();



    @Column(name = "LAST_UPDATE_DATE")

    private LocalDateTime lastUpdateDate;

    @Column(name = "LAST_UPDATE_BY")
    private LocalDateTime lastUpdateBy;

    @Column(name = "CREATED_BY_FULLNAME")
    private String createdByFullname;



}
