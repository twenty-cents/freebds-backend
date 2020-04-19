package com.freebds.backend.exception;

public class FreeBdsApiException  extends RuntimeException {

    private String apiMessage;
    private String errorMessage;

    public FreeBdsApiException(String apiMessage, String errorMessage) {
        super(errorMessage);
        this.apiMessage = apiMessage;
        this.errorMessage = errorMessage;
    }

    public String getApiMessage() {
        return apiMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}