package com.spring.journalapp.dto;

import com.spring.journalapp.enums.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryResponse {

    private String id;

    private String title;

    private String content;

    private LocalDateTime date;

    private Sentiment sentiment;
}
