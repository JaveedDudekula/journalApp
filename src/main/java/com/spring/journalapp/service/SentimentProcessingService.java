package com.spring.journalapp.service;

import com.spring.journalapp.dto.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SentimentProcessingService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SentimentProcessingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveSentimentData(SentimentData sentimentData) {
        String insertSql = "INSERT INTO journal_entries (name, sentiment, created_dt) VALUES (?, ?, ?)";

        System.out.println("Saving to DB (JDBC): name=" + sentimentData.getName() +
                ", sentiment=" + sentimentData.getMostFrequentSentiment());

        int rows = jdbcTemplate.update(
                insertSql,
                sentimentData.getName().toUpperCase(),
                sentimentData.getMostFrequentSentiment(),
                LocalDateTime.now()
        );

        System.out.println("Rows inserted: " + rows);
    }
}
