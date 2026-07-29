package com.example.tasks.controller;



import com.example.tasks.config.PermissionChecker;
import com.example.tasks.domain.Role;
import com.example.tasks.service.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Validated

@CrossOrigin(origins = "http://localhost:4200")
public class RoleController {

    private final PermissionChecker permissionChecker;
    private RoleService roleService;

    RoleController(RoleService roleService, PermissionChecker permissionChecker)
    {
        this.roleService = roleService;
        this.permissionChecker = permissionChecker;
    }


    @GetMapping
    @PreAuthorize("@permissionChecker.hasPermission('ROLE', 'READ')")
    public List<Role> getAllRoles()
    {
        return roleService.getAllRoles();
    }



}
