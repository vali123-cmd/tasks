package com.example.tasks.repository;


import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tasks.domain.StatusType;

import java.util.Optional;


@Repository
public interface StatusTypeRepository extends JpaRepository<StatusType, String> {


    Optional<StatusType> findByStatusName(@NotNull String statusName);


}