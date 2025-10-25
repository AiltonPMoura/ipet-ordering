package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.util.IdGenerator;

import java.util.UUID;

public record PetId(UUID id) {

    public PetId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public PetId {
        FieldValidator.requiresNonNull("pet id", id);
    }

}
