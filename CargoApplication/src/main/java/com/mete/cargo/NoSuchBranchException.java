package com.mete.cargo;

public class NoSuchBranchException extends RuntimeException{
    public NoSuchBranchException(String message) {
        super(message);
    }

    public NoSuchBranchException(String message, Throwable cause) {
        super(message, cause);
    }
}
