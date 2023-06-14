package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.UnsupportedStatusException;

public enum StateBooking {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static StateBooking getStateFromText(String text) {
        for (StateBooking state : StateBooking.values()) {
            if (state.toString().equals(text)) {
                return state;
            }
        }
        throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
    }
}
