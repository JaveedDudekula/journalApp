package com.spring.journalapp.service;

import com.spring.journalapp.dto.SentimentData;
import com.spring.journalapp.exceptions.MailServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SentimentEmailProcessor {

    private final EmailService emailService;

    private final SentimentSqlService sentimentSqlService;

    @Autowired
    public SentimentEmailProcessor(EmailService emailService, SentimentSqlService sentimentSqlService) {
        this.emailService = emailService;
        this.sentimentSqlService = sentimentSqlService;
    }

    @Transactional(transactionManager = "mysqlTransactionManager", propagation = Propagation.REQUIRED)
    public void process(SentimentData sentimentData) {

        sentimentSqlService.saveSentimentData(sentimentData);

        try {
            emailService.sendMail(
                    sentimentData.getEmail(),
                    "Sentiment Analysis for last 7 days",
                    sentimentData.getSentiment()
            );
        } catch (RuntimeException e) {
            throw new MailServiceException("Failed to send email. Rollback in progress...");
        }
    }
}
