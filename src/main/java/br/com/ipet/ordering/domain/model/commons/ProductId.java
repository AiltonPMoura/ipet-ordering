package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public ProductId {
        FieldValidator.requiresNonNull("productId value", value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}
