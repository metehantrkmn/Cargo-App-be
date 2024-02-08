package com.mete.authentication.exceptions;

public class NoSuchUserExists extends RuntimeException{
    public NoSuchUserExists(String message) {
        super(message);
    }

    public NoSuchUserExists(String message, Throwable cause) {
        super(message, cause);
    }
}
