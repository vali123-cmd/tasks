package com.example.tasks.service;



import com.example.tasks.dto.TaskDTO;
import com.example.tasks.dto.UserDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class DeadlineApproachService {

    private final TaskService taskService;
    private final MailService mailService;
    private final UserService userService;


    public long deadlineDays = 3;
    public DeadlineApproachService(TaskService taskService, MailService mailService, UserService userService) {
        this.taskService = taskService;
        this.mailService = mailService;
        this.userService = userService;
    }


    @Scheduled(cron = "0 0 9 * * ?", zone = "EET")
    public void dailyCheck()
    {
     for (TaskDTO task : taskService.getTasks()) {
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
