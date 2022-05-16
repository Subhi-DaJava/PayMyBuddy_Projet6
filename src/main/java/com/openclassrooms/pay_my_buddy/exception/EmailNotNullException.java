package com.openclassrooms.pay_my_buddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class EmailNotNullException extends RuntimeException {
    public EmailNotNullException(String s) {
        super(s);
    }
}
