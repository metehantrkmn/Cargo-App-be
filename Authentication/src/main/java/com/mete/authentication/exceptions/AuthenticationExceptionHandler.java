package com.mete.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AuthenticationExceptionHandler {

    public record AuthenticationException(
            LocalDateTime date,
            HttpStatus status,
            String message,
            Throwable throwable
    ) {
    }

    @ExceptionHandler(value = {NoSuchUserExists.class})
    public ResponseEntity handleNoSuchUserExistsException(NoSuchUserExists ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        LocalDateTime date = LocalDateTime.now();
        AuthenticationException exception = new AuthenticationException(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ex.getCause()
        );
        return new ResponseEntity(exception, status);
    }
}
