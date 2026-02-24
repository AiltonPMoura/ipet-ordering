package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record BookingWindow(Integer value) {

    private static final Integer MINIMUM_BOOKING_WINDOW = 5;
    private static final Integer MAXIMUM_BOOKING_WINDOWS = 60;

    public BookingWindow() {
        this(MINIMUM_BOOKING_WINDOW);
    }

    public BookingWindow {
        FieldValidator.requiresNonNull("bookingBy value", value);

        if (value < MINIMUM_BOOKING_WINDOW)
            throw new RuntimeException("Minimo booking by: " + MINIMUM_BOOKING_WINDOW);

        if (value > MAXIMUM_BOOKING_WINDOWS)
            throw new RuntimeException("Maximo booking by: " + MAXIMUM_BOOKING_WINDOWS);
    }

}
