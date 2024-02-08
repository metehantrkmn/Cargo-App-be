package com.mete;

public class UserNotExists extends RuntimeException{
    public UserNotExists(String message) {
        super(message);
    }

    public UserNotExists(String message, Throwable cause) {
        super(message, cause);
    }
}
