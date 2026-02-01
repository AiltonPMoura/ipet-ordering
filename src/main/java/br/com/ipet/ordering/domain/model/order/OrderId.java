package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record OrderId(UUID value) {

    public OrderId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public OrderId {
        FieldValidator.requiresNonNull("order id value", value);
    }

}
