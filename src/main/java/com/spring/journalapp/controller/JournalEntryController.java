package com.spring.journalapp.controller;

import com.spring.journalapp.dto.JournalEntryRequest;
import com.spring.journalapp.dto.JournalEntryResponse;
import com.spring.journalapp.entity.JournalEntry;
import com.spring.journalapp.entity.User;
import com.spring.journalapp.enums.Sentiment;
import com.spring.journalapp.service.JournalEntryService;
import com.spring.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    private final UserService userService;

    @Autowired
    public JournalEntryController(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping("/all-entries")
    public ResponseEntity<List<JournalEntryResponse>> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> allEntries = user.getJournalEntries();
        List<JournalEntryResponse> responseList = new ArrayList<>();
        for (JournalEntry entry : allEntries) {
            JournalEntryResponse response = journalEntryService.convertEntryToResponse(entry);
            responseList.add(response);
        }
        if (!responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseList, HttpStatus.NO_CONTENT);
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntryResponse> getEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries().stream()
                .filter(x -> x.getId().equals(myId)).toList();
        if (!journalEntries.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(myId);
            if (journalEntry.isPresent()) {
                JournalEntry entry = journalEntry.get();
                return new ResponseEntity<>(journalEntryService.convertEntryToResponse(entry), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntryResponse> createEntry(@RequestBody JournalEntryRequest journalEntryRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            JournalEntryResponse response = journalEntryService.saveEntry(journalEntryRequest, userName);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("id/{myId}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntryRequest newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries().stream()
                .filter(x -> x.getId().equals(myId)).toList();
        if (!journalEntries.isEmpty()) {
            JournalEntry oldEntry = journalEntryService.getEntryById(myId).orElse(null);
            if (oldEntry != null) {
                oldEntry.setTitle(newEntry.getTitle() != null
                        && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null
                        && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
                oldEntry.setSentiment(newEntry.getSentiment() != null
                        ? Sentiment.valueOf(newEntry.getSentiment().toUpperCase()) : oldEntry.getSentiment());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(journalEntryService.convertEntryToResponse(oldEntry), HttpStatus.OK);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Journal entry not found or doesn't belong to the user.");
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<JournalEntry> entry = journalEntryService.getEntryById(myId);
        if (entry.isPresent()) {
            boolean deleted = journalEntryService.deleteEntryById(userName, myId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Journal entry not found or doesn't belong to the user.");
    }
}
