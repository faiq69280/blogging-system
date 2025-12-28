package com.example.user_service.exception;

public class ConflictException extends Exception{
    public ConflictException(String message) {
        super(message);
    }
}
