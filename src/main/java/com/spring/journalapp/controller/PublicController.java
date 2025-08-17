package com.spring.journalapp.controller;

import com.spring.journalapp.dto.AppMessage;
import com.spring.journalapp.dto.UserRequest;
import com.spring.journalapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;

    @Autowired
    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<AppMessage> createUser(@RequestBody @Valid UserRequest userRequest) {
        userService.saveNewUser(userRequest);
        return new ResponseEntity<>(new AppMessage(HttpStatus.CREATED.value(), "User created successfully"),
                HttpStatus.CREATED);
    }
}
