package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId {
        FieldValidator.requiresNonNull("customerId value", value);
    }

    public CustomerId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

}
