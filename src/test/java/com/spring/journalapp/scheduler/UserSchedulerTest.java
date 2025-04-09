package com.spring.journalapp.scheduler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
public class UserSchedulerTest {

    private UserScheduler userScheduler;

    @Autowired
    public UserSchedulerTest(UserScheduler userScheduler) {
        this.userScheduler = userScheduler;
    }

    @Test
    public void testFetchUsersAndSendMailForSA(){
        userScheduler.fetchUsersAndSendMailForSA();
    }
}
