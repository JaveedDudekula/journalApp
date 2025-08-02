package com.spring.journalapp.service;

import com.spring.journalapp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    private EmailService emailService;

    @Autowired
    public SentimentConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "weekly-sentiments", groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData) {
        sendEmail(sentimentData);
    }

    private void sendEmail(SentimentData sentimentData) {
        emailService.sendMail(sentimentData.getEmail(),
                "Sentiment Analysis for last 7 days",
                sentimentData.getSentiment());
    }
}
