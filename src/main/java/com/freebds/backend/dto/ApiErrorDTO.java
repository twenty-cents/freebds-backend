package com.freebds.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * API Error class helping to provide clear error messages.
 */
public class ApiErrorDTO {

    private String errorCode;
    private String errorMessage;
    private String errorDetails;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiErrorDTO(String errorCode, String errorMessage, String errorDetails) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDetails = errorDetails;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
