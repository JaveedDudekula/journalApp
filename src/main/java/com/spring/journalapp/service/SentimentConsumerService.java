package com.spring.journalapp.service;

import com.spring.journalapp.dto.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    private final SentimentEmailProcessor processor;

    @Autowired
    public SentimentConsumerService(SentimentEmailProcessor processor) {
        this.processor = processor;
    }

    @KafkaListener(topics = "weekly-sentiments",
            groupId = "weekly-sentiment-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(SentimentData sentimentData) {
        processor.process(sentimentData);
    }
}
