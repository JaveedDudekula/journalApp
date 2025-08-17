package com.spring.journalapp.dto;

import java.util.Map;

public class ErrorResponseBody {

    private int status;

    private String message;

    private Map<String, String> error;

    public ErrorResponseBody(int status, String message, Map<String, String> error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getError() {
        return error;
    }
}
