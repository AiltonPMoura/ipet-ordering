package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public ProductId {
        FieldValidator.requiresNonNull("productId value", value);
    }

}
