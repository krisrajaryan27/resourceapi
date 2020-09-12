package com.company.codeserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class FoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FoundException(long id) {
        super("" + id);
    }

}