package br.com.ipet.ordering.domain.model.product;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        FieldValidator.requiresNonNull("productId value", value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}
