package com.openclassrooms.pay_my_buddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotExistingException extends RuntimeException {
    public RoleNotExistingException(String s) {
    }
}
