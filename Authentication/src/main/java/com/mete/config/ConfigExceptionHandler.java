package com.mete.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ControllerAdvice
public class ConfigExceptionHandler {

    public record configException(
            LocalDateTime date,
            HttpStatus status,
            String message,
            Throwable throwable
    ){}

    @ExceptionHandler(value = {NoSuchTokenExists.class})
    public configException handleTokenException(Exception ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        configException exception = new configException(LocalDateTime.now(),
                                                        status,
                                                        ex.getMessage(),
                                                        ex.getCause());
        return exception;
    }

}
