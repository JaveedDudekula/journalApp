package com.spring.journalapp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("tested")
class UserRepositoryImplTests {

    private UserRepositoryImpl userRepository;

    @Autowired
    public UserRepositoryImplTests(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void testGetUserForSentimentAnalysis() {
        Assertions.assertNotNull(userRepository.getUserForSentimentAnalysis());
    }
}
