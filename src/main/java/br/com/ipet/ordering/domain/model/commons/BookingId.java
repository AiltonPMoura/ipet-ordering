package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record BookingId(UUID value) {

    public BookingId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public BookingId {
        FieldValidator.requiresNonNull("booking id", value);
    }

}
