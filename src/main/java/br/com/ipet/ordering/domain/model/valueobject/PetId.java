package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record PetId(UUID value) {

    public PetId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public PetId {
        FieldValidator.requiresNonNull("pet id", value);
    }

}
