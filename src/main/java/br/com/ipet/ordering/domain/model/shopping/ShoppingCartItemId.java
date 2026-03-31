package br.com.ipet.ordering.domain.model.shopping;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record ShoppingCartItemId(UUID value) {

    public ShoppingCartItemId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public ShoppingCartItemId {
        FieldValidator.requiresNonNull("id", value);
    }

}
