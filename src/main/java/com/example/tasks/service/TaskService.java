package com.example.tasks.service;


import com.example.tasks.dto.TaskDTO;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j

public class TaskService {
    private List<TaskDTO> tasks = new ArrayList<>();

    public List<TaskDTO> getTasks(){
        log.info("Getting all tasks");
        return tasks;
    }

    public TaskDTO getTaskById(Long id){
        log.info("Getting task by id: {}", id);
        for(TaskDTO task: tasks)
        {
            if(task.getId() == id)
            {
                return task;
            }
        }
        log.warn("Task with id: {} not found", id);
        return null;
    }

    public List<TaskDTO> addTask(TaskDTO task){
        TaskDTO builtTask = buildTask(task);
        log.info("Adding task: {}", builtTask);
        tasks.add(builtTask);
        return tasks;
    }


    private TaskDTO buildTask(TaskDTO task)
    {
        return TaskDTO.builder()
                .id(task.getId())
                .content(task.getContent())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .build();
    }

    public TaskDTO updateTask(Long id, TaskDTO task)
    {
        TaskDTO builtTask = buildTask(task);
        for(TaskDTO t: tasks)
        {
            if(t.getId() == id)
            {
                task.setId(builtTask.getId());
                task.setContent(builtTask.getContent());
                task.setDueDate(builtTask.getDueDate());
                task.setStatus(builtTask.getStatus());
                return task;
            }

        }

        log.warn("Task with id: {} not found", id);
        return null;


    }

    public List<TaskDTO> deleteTask(Long id)
    {
        for(TaskDTO task: tasks)
        {
            if(task.getId() == id)
            {
                log.info("Deleting task with id: {}", id);
                tasks.remove(task);
                return tasks;
            }
        }
        log.warn("Task with id: {} not found", id);
        return tasks;

    }

    public List<TaskDTO> addTasks(List<TaskDTO> tasks)
    {
        log.info("Adding tasks: {}", tasks);
        this.tasks.addAll(tasks);
        return this.tasks;
    }

    public void deleteAllTasks()
    {
        log.info("Deleting all tasks");
        tasks.clear();
    }

    public TaskDTO updateTaskStatus(Long id, String status) {
        for (TaskDTO task : tasks) {
            if (task.getId() == id) {
                task.setStatus(status);
                return task;
            }
        }
        log.warn("Task with id: {} not found", id);
        return null;
    }

    public List<TaskDTO> getTasksLowerThanDate(LocalDateTime date)
    {
        log.info("Getting tasks lower than date: {}", date);
       return tasks.stream()
               .filter(t -> t.getDueDate().isBefore(date)).collect(java.util.stream.Collectors.toList());

    }

    public List<TaskDTO> getTasksHigherThanDate(LocalDateTime date)
    {

       return tasks.stream()
               .filter(t -> t.getDueDate().isAfter(date)).collect(java.util.stream.Collectors.toList());
    }

    public List<TaskDTO> getTasksBetweenDates(LocalDateTime start, LocalDateTime end)
    {
        return tasks.stream()
                .filter(t -> t.getDueDate().isAfter(start) && t.getDueDate().isBefore(end)).collect(java.util.stream.Collectors.toList());
    }

    public TaskDTO updateTaskContent(Long id, String content) {
        for (TaskDTO task : tasks) {
            if (task.getId() == id) {
                task.setContent(content);
                return task;
            }
        }
        log.warn("Task with id: {} not found", id);
        return null;
    }

    public List<TaskDTO> removeRandomTask()
    {

        int randomIndex = (int) (Math.random() * tasks.size());
        TaskDTO removedTask = tasks.remove(randomIndex);
        log.info("Removed task: {}", removedTask);
        return tasks;

    }








}
