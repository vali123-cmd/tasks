package com.example.tasks.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tasks.domain.Task;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("select t from Task t where t.name like %?1")
    List<Task> findByNameEndsWith(String endwith);

    List<Task> findByDueDateBefore(LocalDateTime date);

    List<Task> findByDueDateAfter(LocalDateTime date);

    List<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end);
}
