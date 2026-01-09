package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record BookingBy(Integer value) {

    private static final Integer MINIMUM_BOOKING_BY = 7;
    private static final Integer MAXIMUM_BOOKING_BY = 60;

    public BookingBy {
        FieldValidator.requiresNonNull("bookingBy value", value);

        if (value < MINIMUM_BOOKING_BY)
            throw new RuntimeException("Minimo booking by: " + MINIMUM_BOOKING_BY);

        if (value > MAXIMUM_BOOKING_BY)
            throw new RuntimeException("Maximo booking by: " + MAXIMUM_BOOKING_BY);
    }

}
