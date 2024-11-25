package com.localstack.model;

import java.time.LocalDateTime;

public class ErrorResponse {

    LocalDateTime localDateTime;
    String message;
    String errorCode;

    public ErrorResponse(LocalDateTime localDateTime, String message, String errorCode) {
        this.localDateTime = localDateTime;
        this.message = message;
        this.errorCode = errorCode;
    }
}
