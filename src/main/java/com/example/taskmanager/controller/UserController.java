package com.example.taskmanager.controller;


import com.example.taskmanager.entity.User;
import com.example.taskmanager.security.PasswordConfig;
import com.example.taskmanager.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {

        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/debug")
    public Object debugAuth(Authentication authentication) {
        return authentication.getAuthorities();
    }

}

