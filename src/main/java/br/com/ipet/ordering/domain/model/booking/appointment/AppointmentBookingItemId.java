package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record AppointmentBookingItemId(UUID value) {

    public AppointmentBookingItemId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public AppointmentBookingItemId {
        FieldValidator.requiresNonNull("schedulingId", value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
