package com.example.tasks.controller;


import com.example.tasks.dto.CredentialsDTO;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.service.LoginRegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
    private final LoginRegisterService loginRegisterService;
    public LoginController(LoginRegisterService loginRegisterService) {
        this.loginRegisterService = loginRegisterService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody CredentialsDTO credentialsDTO ) throws Exception{
        return loginRegisterService.login(credentialsDTO);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO credentialsDTO) throws Exception{
        return loginRegisterService.register(credentialsDTO);
    }





}
