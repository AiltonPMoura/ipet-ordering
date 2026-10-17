package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record AppointmentPetServiceId(UUID value) {

    public AppointmentPetServiceId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public AppointmentPetServiceId {
        FieldValidator.requiresNonNull("id", value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
