package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record PetId(UUID value) {

    public PetId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public PetId {
        FieldValidator.requiresNonNull("pet id", value);
    }

}
