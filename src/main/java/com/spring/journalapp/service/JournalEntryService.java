package com.spring.journalapp.service;

import com.spring.journalapp.dto.JournalEntryRequest;
import com.spring.journalapp.dto.JournalEntryResponse;
import com.spring.journalapp.entity.JournalEntry;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.enums.Sentiment;
import com.spring.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    private final UserService userService;

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository, UserService userService) {
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    @Transactional
    public JournalEntryResponse saveEntry(JournalEntryRequest journalEntryRequest, String userName) {
        try {
            User user = userService.findByUserName(userName);
            JournalEntry entry = new JournalEntry();
            entry.setTitle(journalEntryRequest.getTitle());
            entry.setContent(journalEntryRequest.getContent());
            entry.setSentiment(Sentiment.valueOf(journalEntryRequest.getSentiment().toUpperCase()));
            entry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(entry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
            return convertEntryToResponse(saved);
        } catch (Exception e) {
            throw new RuntimeException("An error has occurred while saving the entry", e);
        }
    }

    public void saveEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }

    public Optional<JournalEntry> getEntryById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteEntryById(String userName, ObjectId id) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            throw new RuntimeException("An exception occurred while deleting the entry", e);
        }
        return removed;
    }

    public JournalEntryResponse convertEntryToResponse(JournalEntry entry) {
        return JournalEntryResponse.builder()
                .id(entry.getId().toString())
                .title(entry.getTitle())
                .content(entry.getContent())
                .date(entry.getDate())
                .sentiment(entry.getSentiment())
                .build();
    }
}
