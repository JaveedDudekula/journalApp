package com.spring.journalapp.controller;

import com.spring.journalapp.cache.AppCache;
import com.spring.journalapp.dto.UserRequest;
import com.spring.journalapp.dto.UserResponse;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.UserService;
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
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<String> createAdminUser(@RequestBody UserRequest userRequest) {
        userService.saveAdmin(userRequest);
        return new ResponseEntity<>("User created successfully.", HttpStatus.CREATED);
    }

    @PatchMapping("/add-role")
    public ResponseEntity<String> addRoleToExistingUser(@RequestParam String userName, @RequestParam String role) {
        List<String> allowedRoles = Arrays.asList("USER", "ADMIN");
        role = role.toUpperCase();
        if (!allowedRoles.contains(role)) {
            return ResponseEntity.badRequest().body("Invalid role: " + role);
        }
        User existingUser = userService.findByUserName(userName);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        boolean updated = userService.updateExistingUserRoles(existingUser, role);
        if (updated) {
            return ResponseEntity.ok("Role added successfully.");
        } else {
            return ResponseEntity.ok("User already has this role.");
        }
    }

    @GetMapping("/reloadAppCache")
    public String reloadAppCache() {
        appCache.init();
        return "Cache reload completed";
    }

    @GetMapping("/getCache")
    public Map<String, String> getAppCache() {
        return appCache.getAppCacheMap();
    }
}
