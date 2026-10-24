package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record PetTransportId(UUID value) {

    public PetTransportId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public PetTransportId {
        FieldValidator.requiresNonNull("petTransportId", value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
