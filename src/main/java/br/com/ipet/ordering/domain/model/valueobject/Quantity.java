package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record Quantity(Integer value) {

    public static Quantity ZERO = new Quantity(0);

    public Quantity {
        FieldValidator.requiresNonNull("quantity value", value);
    }

}
