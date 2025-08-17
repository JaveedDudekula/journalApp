package com.spring.journalapp.service;

import com.spring.journalapp.dto.UpdateUserRequest;
import com.spring.journalapp.dto.UserRequest;
import com.spring.journalapp.dto.UserResponse;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveNewUser(UserRequest userRequest) {
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        user.setEmail(userRequest.getEmail());
        user.setSentimentAnalysis(Boolean.parseBoolean(userRequest.getSentimentAnalysis()));
        user.setCity(userRequest.getCity());
        userRepository.save(user);
    }

    public void saveAdmin(UserRequest userRequest) {
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        user.setEmail(userRequest.getEmail());
        user.setSentimentAnalysis(Boolean.parseBoolean(userRequest.getSentimentAnalysis()));
        user.setCity(userRequest.getCity());
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void updateExistingUser(User existingUser, UpdateUserRequest userRequest) {
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        if (userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()) {
            existingUser.setEmail(userRequest.getEmail());
        }
        if (userRequest.getCity() != null && !userRequest.getCity().isEmpty()) {
            existingUser.setCity(userRequest.getCity());
        }
        if (userRequest.getSentimentAnalysis() != null &&
                (userRequest.getSentimentAnalysis().equalsIgnoreCase("true") ||
                        userRequest.getSentimentAnalysis().equalsIgnoreCase("false"))) {
            existingUser.setSentimentAnalysis(Boolean.parseBoolean(userRequest.getSentimentAnalysis()));
        }
        userRepository.save(existingUser);  // Updates the user
    }

    public boolean updateExistingUserRoles(User existingUser, String role) {
        List<String> roles = existingUser.getRoles();
        if (!roles.contains(role)) {
            roles.add(role);
            userRepository.save(existingUser);
            return true;
        }
        return false;
    }

    public List<UserResponse> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userList) {
            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId().toString())
                    .userName(user.getUserName())
                    .roles(user.getRoles())
                    .email(user.getEmail())
                    .sentimentAnalysis(user.isSentimentAnalysis())
                    .city(user.getCity())
                    .build();
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }

    public void deleteByUserName(String userName) {
        userRepository.deleteByUserName(userName);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
