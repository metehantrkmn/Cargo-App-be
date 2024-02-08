package com.mete.cargo;

public class NoSuchCargoException extends RuntimeException{
    public NoSuchCargoException(String message) {
        super(message);
    }

    public NoSuchCargoException(String message, Throwable cause) {
        super(message, cause);
    }
}
