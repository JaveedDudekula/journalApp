package com.spring.journalapp.exceptions;

public class MailServiceException extends RuntimeException {
    public MailServiceException(String message) {
        super(message);
    }
}
