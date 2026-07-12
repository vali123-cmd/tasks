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

    @Column(name = "TASK_NAME")
    private String name;



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

    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdateBy;

    @Column(name = "CREATED_BY_FULLNAME")
    private String createdByFullname;


    @ManyToOne
    @JoinColumn(name = "STATUS_TYPE_ID")
    private StatusType statusType;


}
