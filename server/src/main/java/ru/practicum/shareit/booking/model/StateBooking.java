package ru.practicum.shareit.booking.model;

public enum StateBooking {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static StateBooking getStateFromText(String text) {
        StateBooking stateBooking = null;
        for (StateBooking state : StateBooking.values()) {
            if (state.toString().equals(text)) {
                stateBooking = state;
            }
        }
        return stateBooking;
    }
}
