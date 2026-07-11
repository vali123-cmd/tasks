
package com.example.tasks.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.example.tasks.dto.StatusTypeDTO;
import com.example.tasks.dto.TaskDTO;
import com.example.tasks.service.StatusTypeService;
import com.example.tasks.service.TaskService;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/statuses")
@Validated
public class StatusTypeController {
    private final StatusTypeService statusTypeService;
    public StatusTypeController(StatusTypeService statusTypeService) {
        this.statusTypeService = statusTypeService;
    }
    @GetMapping
    public List<StatusTypeDTO> getAllStatuses(){
        return statusTypeService.getAllStatuses();
    }

    @PostMapping
    public StatusTypeDTO createStatus(@Valid @RequestBody StatusTypeDTO statusTypeDTO){
        return statusTypeService.createStatus(statusTypeDTO);
    }



}
