package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.util.IdGenerator;

import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public ProductId {
        FieldValidator.requiresNonNull("productId value", value);
    }

}
