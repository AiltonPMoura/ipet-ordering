package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record Quantity(Integer value) {

    public static Quantity ZERO = new Quantity(0);

    public Quantity {
        FieldValidator.requiresNonNull("quantity value", value);
    }

}
