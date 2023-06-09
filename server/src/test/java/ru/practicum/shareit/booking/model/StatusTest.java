package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest {
    @Test
    void testStatus() {
        assertEquals(Status.WAITING, Status.WAITING);
        assertEquals(Status.APPROVED, Status.APPROVED);
        assertEquals(Status.REJECTED, Status.REJECTED);
        assertEquals(Status.CANCELED, Status.CANCELED);
    }
}
