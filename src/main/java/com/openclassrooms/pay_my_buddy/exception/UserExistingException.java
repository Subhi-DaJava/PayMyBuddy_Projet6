package com.openclassrooms.pay_my_buddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserExistingException extends RuntimeException {
    public UserExistingException(String s) {
        super(s);
    }
}
