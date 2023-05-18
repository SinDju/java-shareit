package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ObjectBadRequestException.class})
    public ErrorMessage handleException(RuntimeException exception) {
        return new ErrorMessage(
                "Ошибка запроса: {}",
                exception.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {DuplicateException.class})
    public ErrorMessage handleValidationException(RuntimeException exception) {
        return new ErrorMessage(
                "Ошибка запроса: {}",
                exception.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ErrorMessage handleNotFoundException(RuntimeException exception) {
        return new ErrorMessage(
                "Ошибка запроса: {}",
                exception.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {ObjectForbiddenException.class})
    public ErrorMessage handleForbiddenException(RuntimeException exception) {
        return new ErrorMessage(
                "Ошибка запроса: {}",
                exception.getMessage()
        );
    }
}
