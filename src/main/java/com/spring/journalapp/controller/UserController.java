package com.spring.journalapp.controller;

import com.spring.journalapp.api.response.WeatherResponse;
import com.spring.journalapp.dto.UpdateUserRequest;
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
        if (existingUser != null) {
            WeatherResponse weatherResponse = weatherService.getWeather(existingUser.getCity());
            String greeting = "";
            if (weatherResponse != null) {
                greeting = ", temperature in " + existingUser.getCity() + " is "
                        + weatherResponse.getCurrent().getTemperature() + "°C today, feels like "
                        + weatherResponse.getCurrent().getFeelslike() + "°C";
            }
            return new ResponseEntity<>("Hi "
                    + authentication.getName().substring(0, 1).toUpperCase()
                    + authentication.getName().substring(1)
                    + greeting, HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User existingUser = userService.findByUserName(userName);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userService.updateExistingUser(existingUser, userRequest);
        return ResponseEntity.ok("User updated successfully.");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + userName);
        }
        userService.deleteByUserName(userName);
        return ResponseEntity.noContent().build();
    }
}
