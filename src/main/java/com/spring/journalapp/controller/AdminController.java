package com.spring.journalapp.controller;

import com.spring.journalapp.cache.AppCache;
import com.spring.journalapp.dto.AppMessage;
import com.spring.journalapp.dto.ErrorResponseBody;
import com.spring.journalapp.dto.UserRequest;
import com.spring.journalapp.dto.UserResponse;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final AppCache appCache;

    @Autowired
    public AdminController(UserService userService, AppCache appCache) {
        this.userService = userService;
        this.appCache = appCache;
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<UserResponse> allUsers = userService.getAllUsers();
        if (allUsers != null && !allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(new AppMessage(HttpStatus.NO_CONTENT.value(), "No users found"),
                HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<AppMessage> createAdminUser(@RequestBody @Valid UserRequest userRequest) {
        userService.saveAdmin(userRequest);
        return new ResponseEntity<>(new AppMessage(HttpStatus.CREATED.value(), "User created successfully"),
                HttpStatus.CREATED);
    }

    @PatchMapping("/add-role")
    public ResponseEntity<?> addRoleToExistingUser(@RequestParam String userName, @RequestParam String role) {
        List<String> allowedRoles = Arrays.asList("USER", "ADMIN");
        role = role.toUpperCase();
        if (!allowedRoles.contains(role)) {
            ErrorResponseBody errorResponse = new ErrorResponseBody(HttpStatus.BAD_REQUEST.value(),
                    "Invalid role: " + role,
                    Map.of("error", "invalid role"));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        User existingUser = userService.findByUserName(userName);
        if (existingUser == null) {
            return new ResponseEntity<>(new AppMessage(HttpStatus.NOT_FOUND.value(), "User not found"),
                    HttpStatus.NOT_FOUND);
        }

        boolean updated = userService.updateExistingUserRoles(existingUser, role);
        return new ResponseEntity<>(new AppMessage(HttpStatus.OK.value(),
                updated ? "Role added successfully" : "User already has this role"),
                HttpStatus.OK);
    }

    @GetMapping("/reloadAppCache")
    public ResponseEntity<AppMessage> reloadAppCache() {
        appCache.init();
        return new ResponseEntity<>(new AppMessage(HttpStatus.OK.value(),
                "Cache reload completed"),
                HttpStatus.OK);
    }

    @GetMapping("/getCache")
    public Map<String, String> getAppCache() {
        return appCache.getAppCacheMap();
    }
}
