package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

import java.util.UUID;

public record OrderServiceId(UUID id) {

    public OrderServiceId {
        FieldValidator.requiresNonNull("order service id", id);
    }

}
