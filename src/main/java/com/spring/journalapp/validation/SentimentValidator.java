package com.spring.journalapp.validation;

import com.spring.journalapp.annotations.ValidateSentiment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class SentimentValidator implements ConstraintValidator<ValidateSentiment, String> {

    @Override
    public boolean isValid(String sentiment, ConstraintValidatorContext context) {
        if (sentiment == null) {
            return true;
        }
        List<String> validSentiments = List.of("HAPPY", "SAD", "ANXIOUS", "ANGRY");
        return validSentiments.contains(sentiment.toUpperCase());
    }
}
