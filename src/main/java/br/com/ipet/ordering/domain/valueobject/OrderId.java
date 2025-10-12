package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.util.IdGenerator;

import java.util.UUID;

public record OrderId(UUID id) {

    public OrderId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public OrderId {
        FieldValidator.requiresNonNull("id", id);
    }

}
