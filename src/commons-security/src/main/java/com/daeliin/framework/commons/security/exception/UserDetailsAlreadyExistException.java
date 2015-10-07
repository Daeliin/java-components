package com.daeliin.framework.commons.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class UserDetailsAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = -8679392797190599483L;
    
    public UserDetailsAlreadyExistException(String message) {
        super(message);
    }
}
