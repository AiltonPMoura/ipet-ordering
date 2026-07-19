package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record BookingId(UUID value) {

    public BookingId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public BookingId {
        FieldValidator.requiresNonNull("schedulingId", value);
    }

}
