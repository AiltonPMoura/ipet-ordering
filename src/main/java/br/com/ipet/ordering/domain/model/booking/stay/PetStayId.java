package br.com.ipet.ordering.domain.model.booking.stay;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record PetStayId(UUID value) {

    public PetStayId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public PetStayId {
        FieldValidator.requiresNonNull("id", value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
