package ru.practicum.shareit.exception;

public class ObjectBadRequestException extends RuntimeException {
    public ObjectBadRequestException(String message) {
        super(message);
    }
}
