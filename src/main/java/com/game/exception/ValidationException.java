package com.game.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String data) {
        super("Invalid data: " + data);
    }
}
