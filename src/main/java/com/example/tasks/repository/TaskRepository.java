package com.example.tasks.repository;


import com.example.tasks.domain.StatusType;
import com.example.tasks.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.tasks.domain.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("select t from Task t where t.name like %?1")
    List<Task> findByNameEndsWith(String endwith);

    List<Task> findByDueDateBefore(LocalDateTime date);

    List<Task> findByDueDateAfter(LocalDateTime date);

    List<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end);

    List<Task> findByNameContaining(String name);

    List<Task> findByStatusType(StatusType statusType);

    List<Task> findByUser(User user);

    List<Task> findByDueDate(LocalDateTime dueDate);

    @Query("SELECT t FROM Task t " +
            "LEFT JOIN t.user u " +
            "LEFT JOIN t.statusType s " +
            "WHERE (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:statusName IS NULL OR s.statusName = :statusName) " +
            "AND (:username IS NULL OR u.username = :username) " +
            "AND (:startDate IS NULL OR t.dueDate >= :startDate) " +
            "AND (:endDate IS NULL OR t.dueDate <= :endDate)")
    List<Task> searchTasks(
            @Param("name") String name,
            @Param("statusName") String statusName,
            @Param("username") String username,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
