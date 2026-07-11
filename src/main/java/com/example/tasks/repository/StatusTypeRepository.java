package com.example.tasks.repository;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tasks.domain.StatusType;



@Repository
public interface StatusTypeRepository extends JpaRepository<StatusType, String> {

}
