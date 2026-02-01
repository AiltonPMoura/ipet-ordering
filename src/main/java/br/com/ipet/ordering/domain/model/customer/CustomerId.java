package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId {
        FieldValidator.requiresNonNull("customerId value", value);
    }

    public CustomerId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

}
