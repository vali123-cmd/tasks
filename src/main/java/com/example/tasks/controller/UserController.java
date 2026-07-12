package com.example.tasks.controller;


import com.example.tasks.dto.UserDTO;
import com.example.tasks.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable @NotNull(message = "id nu poate fi null") Long id){
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @DeleteMapping("/{id}")
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


}
