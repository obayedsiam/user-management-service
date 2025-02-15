package com.assignment.user.exceptions;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String message;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }
}

