package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorMessageTest {
    @Test
    void testErrorMessage() {
        ErrorMessage errorMessage = new ErrorMessage("exception", "description");
        ErrorMessage errorMessage1 = new ErrorMessage("exception", "description");

        assertEquals(errorMessage.getErrorMessage(), errorMessage1.getErrorMessage());
        assertEquals(errorMessage.getDescription(), errorMessage1.getDescription());
    }
}
