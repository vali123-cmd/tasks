package com.example.tasks.service;


import com.example.tasks.dto.TaskDTO;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


}
