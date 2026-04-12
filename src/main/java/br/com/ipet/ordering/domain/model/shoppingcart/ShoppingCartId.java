package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record ShoppingCartId(UUID value) {

    public ShoppingCartId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public ShoppingCartId {
        FieldValidator.requiresNonNull("id", value);
    }

}
