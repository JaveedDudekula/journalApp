package com.spring.journalapp.controller;

import com.spring.journalapp.cache.AppCache;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    private AppCache appCache;

    @Autowired
    public AdminController(UserService userService, AppCache appCache) {
        this.userService = userService;
        this.appCache = appCache;
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        if (allUsers != null && !allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<String> createAdminUser(@RequestBody User user) {
        userService.saveAdmin(user);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @GetMapping("/reloadAppCache")
    public String reloadAppCache(){
        appCache.init();
        return "Cache reload completed";
    }

    @GetMapping("/getCache")
    public Map<String,String> getAppCache(){
        return appCache.getAppCacheMap();
    }
}
