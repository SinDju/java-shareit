package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHappinessOverflow(final ObjectBadRequestException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);
        return Map.of("Ошибка запроса: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageUnknown handleRequestFailedException(final UnsupportedStatusException e) {
        log.debug("Получен статус 400 Bad request {}", e.getMessage(), e);
        return new ErrorMessageUnknown(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleValidationException(final DuplicateException e) {
        log.debug("Получен статус 409 Conflict {}", e.getMessage(), e);
        return Map.of("Ошибка запроса: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final ObjectNotFoundException e) {
        log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
        return Map.of("Ошибка запроса: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleNotFoundException(final ObjectForbiddenException e) {
        log.debug("Получен статус 403 Forbidden {}", e.getMessage(), e);
        return Map.of("Ошибка запроса: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 Bad request с некорректными данными при валидации {}", e.getMessage(), e);
        return Map.of("Ошибка запроса: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolationException(final ConstraintViolationException e) {
        log.debug("Получен статус 400 Bad request с некорректными данными при валидации {}", e.getMessage(), e);
        return Map.of("Ошибка запроса: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        log.debug("Получен статус 400 Bad request с некорректными данными при валидации {}", e.getMessage(), e);
        return Map.of("Ошибка запроса: {}", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowableException(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return Map.of("Ошибка запроса: {}", e.getMessage());
    }
}
