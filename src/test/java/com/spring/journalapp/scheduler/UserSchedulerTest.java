package com.spring.journalapp.scheduler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
class UserSchedulerTest {

    private final UserScheduler userScheduler;

    @Autowired
    public UserSchedulerTest(UserScheduler userScheduler) {
        this.userScheduler = userScheduler;
    }

    @Test
    void testFetchUsersAndSendMailForSA() {
        userScheduler.fetchUsersAndSendMailForSA();
    }
}
