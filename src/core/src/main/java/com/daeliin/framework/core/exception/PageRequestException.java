package com.daeliin.framework.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PageRequestException extends RuntimeException {
    private static final long serialVersionUID = 1636189676718556919L;

    public PageRequestException(String message) {
        super(message);
    }
}
