package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record OrderItemId(UUID value) {

    public OrderItemId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public OrderItemId {
        FieldValidator.requiresNonNull("orderItem id", value);
    }

}
