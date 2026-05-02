package com.spring.journalapp.controller;

import com.spring.journalapp.dto.AppMessage;
import com.spring.journalapp.dto.UserLoginRequest;
import com.spring.journalapp.dto.UserRequest;
import com.spring.journalapp.exceptions.IncorrectCredentialsException;
import com.spring.journalapp.service.UserDetailsServiceImpl;
import com.spring.journalapp.service.UserService;
import com.spring.journalapp.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public PublicController(JwtUtil jwtUtil, UserService userService, UserDetailsServiceImpl userDetailsService,
                            AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<AppMessage> signup(@RequestBody @Valid UserRequest userRequest) {
        userService.saveNewUser(userRequest);
        return new ResponseEntity<>(new AppMessage(HttpStatus.CREATED.value(), "User created successfully"),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AppMessage> login(@RequestBody UserLoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(new AppMessage(HttpStatus.OK.value(), "Your token is " + jwt),
                    HttpStatus.OK);
        } catch (Exception e) {
            throw new IncorrectCredentialsException(e.getMessage());
        }
    }
}
