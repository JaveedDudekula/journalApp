package com.spring.journalapp.scheduler;

import com.spring.journalapp.dto.SentimentData;
import com.spring.journalapp.entity.JournalEntry;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.enums.Sentiment;
import com.spring.journalapp.repository.impl.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserScheduler {

    private final UserRepositoryImpl userRepository;

    private final KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Autowired
    public UserScheduler(UserRepositoryImpl userRepository, KafkaTemplate<String, SentimentData> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = "0 30 20 * * SUN")
    public void fetchUsersAndSendMailForSA() {
        List<User> userList = userRepository.getUserForSentimentAnalysis();
        for (User user : userList) {
            List<Sentiment> sentimentList = user.getJournalEntries().stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7)) && x.getSentiment() != null)
                    .map(JournalEntry::getSentiment).toList();

            Map<Sentiment, Integer> sentimentCounts = new LinkedHashMap<>();
            for (Sentiment sentiment : sentimentList) {
                sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }

            Sentiment mostFrequentSentiment = null;
            long maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() >= maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder()
                        .email(user.getEmail())
                        .name(user.getUserName())
                        .mostFrequentSentiment(mostFrequentSentiment.toString())
                        .sentiment("Hi " + user.getUserName().substring(0, 1).toUpperCase()
                                + user.getUserName().substring(1)
                                + ",\n\nYour Sentiment for last 7 days is " + mostFrequentSentiment.toString()
                                + ".\n\n\nTeam,\nJournalApp")
                        .build();
                kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
            }
        }
    }
}
