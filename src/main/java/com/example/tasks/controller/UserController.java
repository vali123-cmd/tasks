package com.example.tasks.controller;


import com.example.tasks.dto.CredentialsDTO;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated

@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    @PreAuthorize("@permissionChecker.hasPermission('USER', 'READ')")
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@permissionChecker.hasPermission('USER', 'READ')")
    public UserDTO getUserById(@PathVariable @NotNull(message = "id nu poate fi null") Long id){
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@permissionChecker.hasPermission('USER', 'DELETE')")
    public void deleteUserById(@PathVariable @NotNull(message = "id nu poate fi null") Long id ){
        userService.deleteUserById(id);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable @NotNull(message = "id nu poate fi null") Long id, @Valid @RequestBody UserDTO userDTO){
        return userService.updateUser(id, userDTO);
    }

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable @NotNull(message = "username nu poate fi null") String username){
        return userService.getUserByUsername(username);
    }

    @PostMapping("login")
    public Object UserResponseDTO (@Valid @RequestBody CredentialsDTO LoginDTO){
        return userService.login(LoginDTO);
    }


}
