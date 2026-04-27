package com.spring.journalapp.handler;

import com.spring.journalapp.dto.ErrorResponseBody;
import com.spring.journalapp.exceptions.MailServiceException;
import com.spring.journalapp.exceptions.WeatherServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class JournalAppExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(JournalAppExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        ErrorResponseBody errorResponse = new ErrorResponseBody(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponseBody> handleDuplicateKeyException(DuplicateKeyException ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponseBody errorResponse = new ErrorResponseBody(
                HttpStatus.BAD_REQUEST.value(),
                "Duplicate Username",
                Map.of("description", "Username already taken. Please try with some other username")
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MailServiceException.class)
    public ResponseEntity<ErrorResponseBody> handleMailServiceException(MailServiceException ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponseBody errorResponse = new ErrorResponseBody(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                Map.of("description", "Some issue observed at mail service, Please try again later")
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(WeatherServiceException.class)
    public ResponseEntity<ErrorResponseBody> handleWeatherServiceException(WeatherServiceException ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponseBody errorResponse = new ErrorResponseBody(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                Map.of("description", "Your Weather API request failed. Please try again or contact support.")
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Handle unknown errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> handleUnknownException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error at [{} {}]: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        ErrorResponseBody errorResponse = new ErrorResponseBody(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                Map.of("description", "Unexpected Error Occurred")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
