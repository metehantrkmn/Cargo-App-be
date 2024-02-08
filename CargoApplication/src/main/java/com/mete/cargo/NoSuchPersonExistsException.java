package com.mete.cargo;


public class NoSuchPersonExistsException extends RuntimeException{
    public NoSuchPersonExistsException(String message) {
        super(message);
    }

    public NoSuchPersonExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
