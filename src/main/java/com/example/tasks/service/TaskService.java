package com.example.tasks.service;


import com.example.tasks.domain.StatusType;
import com.example.tasks.domain.Task;
import com.example.tasks.dto.TaskDTO;
import com.example.tasks.mapper.TaskMapper;
import com.example.tasks.repository.StatusTypeRepository;
import com.example.tasks.repository.TaskRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final StatusTypeRepository statusTypeRepository;

    public List<TaskDTO> getTasks(){
        log.info("Getting all tasks");
        return taskRepository.findAll().stream().map(taskMapper::toDTO).toList();
    }

    public TaskDTO getTaskById(String id){
        log.info("Getting task by id: {}", id);


        TaskDTO task =  taskRepository.findById(id).map(taskMapper::toDTO).orElseThrow(() -> new RuntimeException("Task not found"));

        return task;
    }
    @Transactional
    public List<TaskDTO> addTask(TaskDTO task){
        log.info("Adding task: {}", task);
        StatusType status = statusTypeRepository.findByStatusName(task.getStatusName()).orElse(null);
        taskRepository.save(taskMapper.toEntity(task, status));
        return getTasks();

    }


    @Transactional
    public TaskDTO updateTask(TaskDTO taskDTO, String id)
    {
        log.info("Updating task: {}", taskDTO);
        Task targetTask = taskRepository.findById(id).orElse(null);
        if(targetTask == null)
        {
            log.warn("Task with id: {} not found", id);
            return null;
        }
        targetTask.setName(taskDTO.getContent());
        if (taskDTO.getStatusName() != null) {

            StatusType newStatus = statusTypeRepository.findByStatusName(taskDTO.getStatusName())
                    .orElseThrow(() -> new RuntimeException("Statusul '" + taskDTO.getStatusName() + "' nu este valid!"));


            targetTask.setStatusType(newStatus);
        }
        targetTask.setDueDate(taskDTO.getDueDate());
        targetTask.setLastUpdateDate(LocalDateTime.now());

        Task updatedTask = taskRepository.save(targetTask);
        return taskMapper.toDTO(updatedTask);


    }
    @Transactional
    public List<TaskDTO> deleteTask(String id)
    {
        log.info("Deleting task with id: {}", id);
        taskRepository.deleteById(id);
        return getTasks();

    }
    @Transactional
    public List<TaskDTO> addTasks(List<TaskDTO> tasks)
    {
        log.info("Adding tasks: {}", tasks);
        for(TaskDTO t : tasks)
        {
            addTask(t);

        }
        return getTasks();
    }
    @Transactional
    public void deleteAllTasks()
    {
        log.info("Deleting all tasks");
        taskRepository.deleteAll();
    }
    @Transactional
    public TaskDTO updateTaskStatus(String id, String status) {
        Task targetTask = taskRepository.findById(id).orElse(null);
        if (targetTask != null) {
            StatusType newStatus = statusTypeRepository.findByStatusName(status)
                    .orElseThrow(() -> new RuntimeException("Statusul '" + status + "' nu este valid!"));
            targetTask.setStatusType(newStatus);
            return taskMapper.toDTO(taskRepository.save(targetTask));
        }
        return null;
    }

    public List<TaskDTO> getTasksLowerThanDate(LocalDateTime date)
    {
        return taskRepository.findByDueDateBefore(date).stream()
                .map(taskMapper::toDTO)
                .toList();


    }

    public List<TaskDTO> getTasksHigherThanDate(LocalDateTime date)
    {

        return taskRepository.findByDueDateAfter(date).stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    public List<TaskDTO> getTasksBetweenDates(LocalDateTime start, LocalDateTime end)
    {
        return taskRepository.findByDueDateBetween(start,end).stream()
                .map(taskMapper::toDTO)
                .toList();
    }
    @Transactional
    public TaskDTO updateTaskContent(String id, String content) {

        Task targetTask = taskRepository.findById(id).orElse(null);
        if (targetTask != null) {
            targetTask.setName(content);
            return taskMapper.toDTO(taskRepository.save(targetTask));
        }

        return null;
    }

    @Transactional
    public List<TaskDTO> removeRandomTask()
    {

        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            return Collections.emptyList();
        }
        int randomIndex = (int) (Math.random() * tasks.size());
        Task randomTask = tasks.get(randomIndex);
        taskRepository.delete(randomTask);
        log.info("Deleted random task with id: {}", randomTask.getId());
        return getTasks();

    }

    public List<TaskDTO> tasksNamesThatEndWith(String ending) {
        return taskRepository.findByNameEndsWith(ending).stream().map(taskMapper::toDTO).toList();
    }








}
