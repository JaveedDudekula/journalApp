package com.spring.journalapp.dto;

import com.spring.journalapp.annotations.ValidateSentiment;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJournalEntry {

    @Pattern(regexp = "^[\\w\\-.\\s]*$", message = "title contains invalid characters")
    private String title;

    private String content;

    @ValidateSentiment
    private String sentiment;
}
