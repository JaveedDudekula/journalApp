package com.spring.journalapp.controller;

import com.spring.journalapp.api.response.WeatherResponse;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.UserService;
import com.spring.journalapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    private WeatherService weatherService;

    @Autowired
    public UserController(UserService userService, WeatherService weatherService) {
        this.userService = userService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("New York");
        String greeting = "";
        if (weatherResponse != null) {
            greeting = ", temperature in New York is " + weatherResponse.getCurrent().getTemperature() +
                    " °C today, feels like " + weatherResponse.getCurrent().getFeelslike() + " °C";
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User existingUser = userService.findByUserName(userName);
        if (existingUser != null) {
            existingUser.setUserName(user.getUserName());
            existingUser.setPassword(user.getPassword());
            userService.saveNewUser(existingUser);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
