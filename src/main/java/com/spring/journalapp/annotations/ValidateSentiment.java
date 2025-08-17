package com.spring.journalapp.annotations;

import com.spring.journalapp.validation.SentimentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SentimentValidator.class)
public @interface ValidateSentiment {

    public String message() default "Invalid sentiment. Input should be one from these sentiments: HAPPY, SAD, ANXIOUS, ANGRY";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
