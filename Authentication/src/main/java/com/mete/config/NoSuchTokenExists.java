package com.mete.config;

public class NoSuchTokenExists extends RuntimeException{
    public NoSuchTokenExists(String message) {
        super(message);
    }

    public NoSuchTokenExists(String message, Throwable cause) {
        super(message, cause);
    }
}
