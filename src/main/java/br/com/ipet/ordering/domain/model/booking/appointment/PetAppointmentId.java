package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record PetAppointmentId(UUID value) {

    public PetAppointmentId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public PetAppointmentId {
        FieldValidator.requiresNonNull("id", value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
