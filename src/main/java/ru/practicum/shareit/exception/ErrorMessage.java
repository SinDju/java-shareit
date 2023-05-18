package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {
    String error; // название ошибки
    String description; // подробное описание
}
