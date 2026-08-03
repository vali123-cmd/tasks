package com.example.tasks.controller;

import com.example.tasks.service.DeadlineApproachService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TestSchedulerController {

    private final DeadlineApproachService deadlineApproachService;

    public TestSchedulerController(DeadlineApproachService deadlineApproachService) {
        this.deadlineApproachService = deadlineApproachService;
    }


    @PostMapping("/test-daily-check")
    public String testDailyCheck() {
        deadlineApproachService.dailyCheck();
        return "Manual check triggered!";
    }
}