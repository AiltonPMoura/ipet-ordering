package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record OrderItemId(UUID value) {

    public OrderItemId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public OrderItemId {
        FieldValidator.requiresNonNull("orderItem id", value);
    }

}
