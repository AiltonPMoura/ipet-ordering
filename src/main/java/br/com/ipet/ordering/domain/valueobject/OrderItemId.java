package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.util.IdGenerator;

import java.util.UUID;

public record OrderItemId(UUID id) {

    public OrderItemId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public OrderItemId {
        FieldValidator.requiresNonNull("orderItem id", id);
    }

}
