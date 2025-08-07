package com.spring.journalapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentimentData {

    private String email;

    private String sentiment;

    private String name;

    private String mostFrequentSentiment;
}
