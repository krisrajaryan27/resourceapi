package com.company.codeserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(final Class<?> clazz, final long id) {
        super("Instance of " + clazz.getName() + "with id: " + id + " cannot be found.");

    }


}