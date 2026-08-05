package com.example.tasks.service;



import com.example.tasks.dto.TaskDTO;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.mapper.TaskMapper;
import com.example.tasks.repository.TaskRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DeadlineApproachService {

    private final TaskRepository taskRepository;
    private final MailService mailService;
    private final UserService userService;
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public long deadlineDays = 3;
    public DeadlineApproachService(TaskRepository taskRepository, MailService mailService, UserService userService, TaskService taskService, TaskMapper taskMapper ) {
        this.taskRepository = taskRepository;
        this.mailService = mailService;
        this.userService = userService;
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }


    @Scheduled(cron = "0 0 9 * * ?", zone = "EET")
    public void dailyCheck()
    {
        List<TaskDTO> tasks;
        tasks = taskRepository.findAll().stream().map(taskMapper::toDTO).toList();
     for (TaskDTO task : tasks) {
         if(taskService.checkForApproachingDeadline(task, deadlineDays) && task.getStatusName() != "Cancelled")
         {
                try {
                    UserDTO user = userService.getUserById(task.getAssignedTo());
                    mailService.sendEmail(user.getEmail(), "Task Deadline Approaching", "The deadline for the task '" + task.getContent() + "' is approaching in " + deadlineDays + " days.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
         }


     }
     System.out.println("DeadlineApproachService: Daily check completed.");
    }



}
