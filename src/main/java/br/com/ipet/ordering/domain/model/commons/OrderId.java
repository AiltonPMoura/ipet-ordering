package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record OrderId(UUID value) {

    public OrderId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public OrderId {
        FieldValidator.requiresNonNull("order id value", value);
    }

}
