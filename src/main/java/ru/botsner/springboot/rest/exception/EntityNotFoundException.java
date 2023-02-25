package ru.botsner.springboot.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(int id) {
        super("Entity with ID = " + id + " not found");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
