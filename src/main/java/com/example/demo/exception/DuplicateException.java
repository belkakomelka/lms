package com.example.demo.exception;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String errorMessage) {
        super(errorMessage);
    }
}
