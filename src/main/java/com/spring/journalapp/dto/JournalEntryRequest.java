package com.spring.journalapp.dto;

import com.spring.journalapp.annotations.ValidateSentiment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryRequest {

    @NotBlank(message = "title shouldn't be null or blank")
    @Pattern(regexp = "^[\\w\\-.,\\s]+$", message = "title contains invalid characters")
    private String title;

    private String content;

    @NotBlank(message = "sentiment shouldn't be null or blank")
    @ValidateSentiment
    private String sentiment;
}
