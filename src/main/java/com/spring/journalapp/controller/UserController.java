package com.spring.journalapp.controller;

import com.spring.journalapp.api.response.WeatherResponse;
import com.spring.journalapp.dto.AppMessage;
import com.spring.journalapp.dto.UpdateUserRequest;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.UserService;
import com.spring.journalapp.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final WeatherService weatherService;

    @Autowired
    public UserController(UserService userService, WeatherService weatherService) {
        this.userService = userService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<String> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User existingUser = userService.findByUserName(userName);
        WeatherResponse weatherResponse = weatherService.getWeather(existingUser.getCity());
        String greeting = "";
        if (weatherResponse != null) {
            greeting = ", temperature in " + existingUser.getCity() + " is "
                    + weatherResponse.getCurrent().getTemperature() + "°C today, feels like "
                    + weatherResponse.getCurrent().getFeelslike() + "°C";
        }
        return new ResponseEntity<>("Hi "
                + userName.substring(0, 1).toUpperCase()
                + userName.substring(1)
                + greeting, HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<AppMessage> updateUser(@RequestBody @Valid UpdateUserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User existingUser = userService.findByUserName(userName);
        userService.updateExistingUser(existingUser, userRequest);
        return new ResponseEntity<>(new AppMessage(HttpStatus.OK.value(),
                "User updated successfully"),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<AppMessage> deleteByUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userService.deleteByUserName(userName);
        return new ResponseEntity<>(new AppMessage(HttpStatus.NO_CONTENT.value(),
                "User deleted successfully"),
                HttpStatus.NO_CONTENT);
    }
}
