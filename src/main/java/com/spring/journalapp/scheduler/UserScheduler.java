package com.spring.journalapp.scheduler;

import com.spring.journalapp.entity.JournalEntry;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.enums.Sentiment;
import com.spring.journalapp.repository.UserRepositoryImpl;
import com.spring.journalapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    private EmailService emailService;

    private UserRepositoryImpl userRepository;

    @Autowired
    public UserScheduler(EmailService emailService, UserRepositoryImpl userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendMailForSA() {
        List<User> userList = userRepository.getUserForSentimentAnalysis();
        for (User user : userList) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentimentList = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7)) && x.getSentiment() != null)
                    .map(JournalEntry::getSentiment).toList();
            Map<Sentiment, Long> sentimentsCount = sentimentList.stream()
                    .collect(Collectors.groupingBy(x -> x, Collectors.counting()));
            Sentiment mostFrequentSentiment = null;
            long maxCount = 0;
            for (Map.Entry<Sentiment, Long> entry : sentimentsCount.entrySet()) {
                if (entry.getValue() >= maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                emailService.sendMail(user.getEmail(), "Sentiment Analysis for last 7 days",
                        "Hi " + user.getUserName() + ",\nYour Sentiment for last 7 days is " + mostFrequentSentiment.toString());
            }
        }
    }
}
