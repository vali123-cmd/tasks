package com.example.tasks.service;

import com.example.tasks.config.PermissionChecker;
import com.example.tasks.domain.StatusType;
import com.example.tasks.domain.Task;
import com.example.tasks.domain.User;
import com.example.tasks.dto.TaskDTO;
import com.example.tasks.mapper.TaskMapper;
import com.example.tasks.repository.StatusTypeRepository;
import com.example.tasks.repository.TaskRepository;
import com.example.tasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final StatusTypeRepository statusTypeRepository;
    private final UserRepository userRepository;
    private final PermissionChecker permissionChecker;

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private String getCurrentUserEmail() {
        return getAuth().getName();
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isAdmin() {
        return permissionChecker.hasPermission("TASKS", "READ_ALL");
    }

    public List<TaskDTO> getTasks() {
        if (isAdmin()) {
            return taskRepository.findAll().stream().map(taskMapper::toDTO).toList();
        } else {
            return taskRepository.findByUser(getCurrentUser()).stream().map(taskMapper::toDTO).toList();
        }
    }

    public TaskDTO getTaskById(String id) {
        Task targetTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!isAdmin() && (targetTask.getUser() == null || !targetTask.getUser().getUserId().equals(getCurrentUser().getUserId()))) {
            throw new RuntimeException("Permission denied");
        }
        return taskMapper.toDTO(targetTask);
    }


    @Transactional
    public List<TaskDTO> addTask(TaskDTO task) {
        StatusType status = statusTypeRepository.findByStatusName(task.getStatusName()).orElse(null);
        User user = getCurrentUser();

        if (isAdmin()) {
            taskRepository.save(taskMapper.toEntity(task, status, user));
        } else {
            Long userId = user.getUserId();
            if (task.getAssignedTo() != null && !task.getAssignedTo().equals(userId)) {
                throw new RuntimeException("Permission denied");
            }
            task.setAssignedTo(userId);
            taskRepository.save(taskMapper.toEntity(task, status, user));
        }
        return getTasks();
    }

    @Transactional
    public TaskDTO updateTask(TaskDTO taskDTO, String id) {
        User currentUser = getCurrentUser();
        Task targetTask = taskRepository.findById(id).orElse(null);

        if (targetTask == null) {
            return null;
        }

        boolean admin = isAdmin();
        boolean isAssignedToTask = targetTask.getUser() != null && targetTask.getUser().getUserId().equals(currentUser.getUserId());

        if (!admin && !isAssignedToTask) {
            throw new RuntimeException("Permission denied");
        }

        if (taskDTO.getStatusName() != null) {
            StatusType newStatus = statusTypeRepository.findByStatusName(taskDTO.getStatusName())
                    .orElseThrow(() -> new RuntimeException("Invalid status"));
            targetTask.setStatusType(newStatus);
        }

        if (admin) {
            if (taskDTO.getContent() != null) {
                targetTask.setName(taskDTO.getContent());
            }
            if (taskDTO.getDueDate() != null) {
                targetTask.setDueDate(taskDTO.getDueDate());
            }
            if (taskDTO.getAssignedTo() != null) {
                User newUser = userRepository.findById(taskDTO.getAssignedTo()).orElse(null);
                targetTask.setUser(newUser);
            }
        }

        targetTask.setLastUpdateDate(LocalDateTime.now());
        Task updatedTask = taskRepository.save(targetTask);
        return taskMapper.toDTO(updatedTask);
    }

    @Transactional
    public List<TaskDTO> deleteTask(String id) {
        Task targetTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!isAdmin() && (targetTask.getUser() == null || !targetTask.getUser().getUserId().equals(getCurrentUser().getUserId()))) {
            throw new RuntimeException("Permission denied");
        }
        taskRepository.deleteById(id);
        return getTasks();
    }

    @Transactional
    public List<TaskDTO> addTasks(List<TaskDTO> tasks) {
        for (TaskDTO t : tasks) {
            addTask(t);
        }
        return getTasks();
    }

    @Transactional
    public void deleteAllTasks() {
        if (!isAdmin()) {
            throw new RuntimeException("Permission denied");
        }
        taskRepository.deleteAll();
    }

    @Transactional
    public TaskDTO updateTaskStatus(String id, String status) {
        Task targetTask = taskRepository.findById(id).orElse(null);
        if (targetTask != null) {
            if (!isAdmin() && (targetTask.getUser() == null || !targetTask.getUser().getUserId().equals(getCurrentUser().getUserId()))) {
                throw new RuntimeException("Permission denied");
            }
            StatusType newStatus = statusTypeRepository.findByStatusName(status)
                    .orElseThrow(() -> new RuntimeException("Invalid status"));
            targetTask.setStatusType(newStatus);
            return taskMapper.toDTO(taskRepository.save(targetTask));
        }
        return null;
    }

    public List<TaskDTO> getTasksLowerThanDate(LocalDateTime date) {
        if (isAdmin()) {
            return taskRepository.findByDueDateBefore(date).stream().map(taskMapper::toDTO).toList();
        } else {
            return taskRepository.findByUser(getCurrentUser()).stream()
                    .filter(task -> task.getDueDate().isBefore(date))
                    .map(taskMapper::toDTO).toList();
        }
    }

    public List<TaskDTO> getTasksHigherThanDate(LocalDateTime date) {
        if (isAdmin()) {
            return taskRepository.findByDueDateAfter(date).stream().map(taskMapper::toDTO).toList();
        } else {
            return taskRepository.findByUser(getCurrentUser()).stream()
                    .filter(task -> task.getDueDate().isAfter(date))
                    .map(taskMapper::toDTO).toList();
        }
    }

    public List<TaskDTO> getTasksBetweenDates(LocalDateTime start, LocalDateTime end) {
        if (isAdmin()) {
            return taskRepository.findByDueDateBetween(start, end).stream().map(taskMapper::toDTO).toList();
        } else {
            return taskRepository.findByUser(getCurrentUser()).stream()
                    .filter(task -> !task.getDueDate().isBefore(start) && !task.getDueDate().isAfter(end))
                    .map(taskMapper::toDTO).toList();
        }
    }

    @Transactional
    public TaskDTO updateTaskContent(String id, String content) {
        if (!isAdmin()) {
            throw new RuntimeException("Permission denied");
        }
        Task targetTask = taskRepository.findById(id).orElse(null);
        if (targetTask != null) {
            targetTask.setName(content);
            return taskMapper.toDTO(taskRepository.save(targetTask));
        }
        return null;
    }

    @Transactional
    public List<TaskDTO> removeRandomTask() {
        if (!isAdmin()) {
            throw new RuntimeException("Permission denied");
        }
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            return Collections.emptyList();
        }
        int randomIndex = (int) (Math.random() * tasks.size());
        taskRepository.delete(tasks.get(randomIndex));
        return getTasks();
    }

    public List<TaskDTO> tasksNamesThatEndWith(String ending) {
        if (isAdmin()) {
            return taskRepository.findByNameEndsWith(ending).stream().map(taskMapper::toDTO).toList();
        } else {
            return taskRepository.findByUser(getCurrentUser()).stream()
                    .filter(task -> task.getName().endsWith(ending))
                    .map(taskMapper::toDTO).toList();
        }
    }

    public List<TaskDTO> findTasksByName(String name) {
        if (isAdmin()) {
            return taskRepository.findByNameContaining(name).stream().map(taskMapper::toDTO).toList();
        } else {
            return taskRepository.findByUser(getCurrentUser()).stream()
                    .filter(task -> task.getName().contains(name))
                    .map(taskMapper::toDTO).toList();
        }
    }

    public List<TaskDTO> findTasksByStatus(String statusName) {
        StatusType statusType = statusTypeRepository.findByStatusName(statusName)
                .orElseThrow(() -> new RuntimeException("Invalid status"));
        if (isAdmin()) {
            return taskRepository.findByStatusType(statusType).stream().map(taskMapper::toDTO).toList();
        } else {
            return taskRepository.findByUser(getCurrentUser()).stream()
                    .filter(task -> task.getStatusType().equals(statusType))
                    .map(taskMapper::toDTO).toList();
        }
    }

    public List<TaskDTO> findTasksByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!isAdmin() && !user.getUserId().equals(getCurrentUser().getUserId())) {
            throw new RuntimeException("Permission denied");
        }
        return taskRepository.findByUser(user).stream().map(taskMapper::toDTO).toList();
    }

    public List<TaskDTO> findTasksByDueDate(LocalDateTime dueDate) {
        if (isAdmin()) {
            return taskRepository.findByDueDate(dueDate).stream().map(taskMapper::toDTO).toList();
        } else {
            return taskRepository.findByUser(getCurrentUser()).stream()
                    .filter(task -> task.getDueDate().equals(dueDate))
                    .map(taskMapper::toDTO).toList();
        }
    }

    public List<TaskDTO> searchTasks(String name, String statusName, String username, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(23, 59, 59) : null;

        if (!isAdmin() && username != null && !username.equals(getCurrentUser().getUsername())) {
            throw new RuntimeException("Permission denied");
        }

        List<Task> tasks = taskRepository.searchTasks(name, statusName, username, startDateTime, endDateTime);

        if (!isAdmin()) {
            Long currentUserId = getCurrentUser().getUserId();
            return tasks.stream()
                    .filter(task -> task.getUser() != null && task.getUser().getUserId().equals(currentUserId))
                    .map(taskMapper::toDTO)
                    .collect(Collectors.toList());
        }

        return tasks.stream().map(taskMapper::toDTO).collect(Collectors.toList());
    }
    public Boolean checkForApproachingDeadline(TaskDTO task, Long deadline)
    {

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime dueDate = task.getDueDate();
        Long daysUntilDeadline = currentDateTime.until(dueDate, java.time.temporal.ChronoUnit.DAYS);

        if (daysUntilDeadline <= deadline && daysUntilDeadline >= 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}