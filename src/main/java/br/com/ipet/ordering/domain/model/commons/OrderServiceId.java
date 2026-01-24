package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

import java.util.UUID;

public record OrderServiceId(UUID id) {

    public OrderServiceId {
        FieldValidator.requiresNonNull("order service id", id);
    }

}
