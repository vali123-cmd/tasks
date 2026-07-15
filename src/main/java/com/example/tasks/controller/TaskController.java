package com.example.tasks.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.example.tasks.domain.Task;
import com.example.tasks.dto.TaskDTO;
import com.example.tasks.service.TaskService;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public List<TaskDTO> getTasks(){
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable("id") @NotNull(message="id nu poate fi null") String id)
    {
        return taskService.getTaskById(id);
    }

    @GetMapping("/before")
    public List<TaskDTO> getTasksBeforeDate(@RequestParam
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date)
    {
      return taskService.getTasksLowerThanDate(date);
    }

    @GetMapping("/after")
    public List<TaskDTO> getTasksAfterDate(@RequestParam
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date)
    {
        return taskService.getTasksHigherThanDate(date);
    }

    @GetMapping("/between")
    public List<TaskDTO> getTasksBetweenDates(@RequestParam
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                @RequestParam
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
                                                )
    {
        return taskService.getTasksBetweenDates(start, end);
    }

    @PostMapping
    public List<TaskDTO> addTask(@Valid @RequestBody TaskDTO task)
    {
        return taskService.addTask(task);

    }

    @PostMapping("/add-tasks")
    public List<TaskDTO> addTasks( @Valid @RequestBody List<@Valid TaskDTO> tasks){
        return taskService.addTasks(tasks);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") @NotNull(message="id nu poate fi null") String id)
    {
        taskService.deleteTask(id);
    }

    @DeleteMapping
    public void deleteAllTasks()
    {
        taskService.deleteAllTasks();
    }

    @DeleteMapping("/random")
    public void deleteRandomTask()
    {
        taskService.removeRandomTask();
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable("id") @NotNull(message="id nu poate fi null") String id, @Valid @RequestBody TaskDTO task)
    {
        return taskService.updateTask(task, id);
    }




    @PutMapping("/{id}/status")
    public TaskDTO updateTaskStatus(@PathVariable("id") @NotNull(message="id nu poate fi null") String id, @RequestBody String status)
    {
        return taskService.updateTaskStatus(id, status);
    }

    @PutMapping("/{id}/content")
    public TaskDTO updateTaskContent(@PathVariable("id") @NotNull(message="id nu poate fi null") String id, @RequestBody String content)
    {
        return taskService.updateTaskContent(id, content);
    }


    @GetMapping("/endswith")
    public List<TaskDTO> taskContentEndsWith(@RequestParam @NotBlank(message = "Content trebuie sa aiba o valoare") String content)
        {
        return taskService.tasksNamesThatEndWith(content);
        }


}
