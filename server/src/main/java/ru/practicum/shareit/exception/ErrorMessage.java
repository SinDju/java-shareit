package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {
    private String errorMessage; // название ошибки
    private String description; // подробное описание
}
