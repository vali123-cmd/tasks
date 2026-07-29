package com.example.tasks.config;


import com.example.tasks.domain.User;

import com.example.tasks.service.LoginRegisterService;
import com.example.tasks.service.TaskService;
import com.example.tasks.repository.UserRepository;
import com.example.tasks.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component("permissionChecker")
@Slf4j

public class PermissionChecker {



    public boolean hasPermission(String resource, String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            System.out.println("-> Acces respins: User neautentificat");
            return false;
        }


        String requiredPermission = action.toUpperCase() + "_" + resource.toUpperCase();
        boolean isReadTaskRequest = action.equalsIgnoreCase("READ") && resource.equalsIgnoreCase("TASK");


        var userAuthorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(String::trim)
                .toList();


        boolean hasAccess = userAuthorities.stream()
                .anyMatch(authority ->
                        authority.equals(requiredPermission) ||
                                (isReadTaskRequest && authority.equals("READ_ALL_TASKS"))
                );



        return hasAccess;
    }





}
