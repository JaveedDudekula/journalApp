package com.spring.journalapp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
public class EmailServiceTests {

    private EmailService emailService;

    @Autowired
    public EmailServiceTests(EmailService emailService) {
        this.emailService = emailService;
    }

    @Test
    void testSendMail(){
        emailService.sendMail("javeeddudekula69@gmail.com","Testing java email service","Hi,\nMessage received.");
    }
}
