package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorMessageUnknownTest {

    @Test
    void testErrorMessageUnknown() {
        ErrorMessageUnknown messageUnknown = new ErrorMessageUnknown("exception");
        ErrorMessageUnknown messageUnknown2 = new ErrorMessageUnknown("exception");
        assertEquals(messageUnknown.getError(), messageUnknown2.getError());
    }
}
