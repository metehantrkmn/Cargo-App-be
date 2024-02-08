package com.mete.personel;

public class NoSuchPersonelException extends RuntimeException{
    public NoSuchPersonelException(String message) {
        super(message);
    }

    public NoSuchPersonelException(String message, Throwable cause) {
        super(message, cause);
    }
}
