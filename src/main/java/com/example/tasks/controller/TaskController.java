package com.example.tasks.controller;

import java.util.List;
import com.example.tasks.dto.TaskDTO;
import com.example.tasks.service.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getTasks(){
        return taskService.getTasks();
    }

    @GetMapping("/id")
    public TaskDTO getTaskById(@PathVariable Long id)
    {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public List<TaskDTO> addTask(@RequestBody TaskDTO task)
    {
        return taskService.addTask(task);

    }

    @PostMapping("/add-tasks")
    public List<TaskDTO> addTasks(@RequestBody List<TaskDTO> tasks){
        return taskService.addTasks(tasks);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id)
    {
        taskService.deleteTask(id);
    }

    @DeleteMapping
    public void deleteAllTasks()
    {
        taskService.deleteAllTasks();
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @RequestBody TaskDTO task)
    {
        return taskService.updateTask(id, task);
    }

    @PutMapping("/{id}/status")
    public TaskDTO updateTaskStatus(@PathVariable Long id, @RequestBody String status)
    {
        return taskService.updateTaskStatus(id, status);
    }



}
