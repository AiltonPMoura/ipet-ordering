package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record Quantity(Integer value) {

    public static Quantity ZERO = new Quantity(0);

    public Quantity {
        FieldValidator.requiresNonNull("quantity value", value);
    }

    public Quantity sum(Quantity quantity) {
        if (quantity.value < 1)
            throw new RuntimeException();

        return new Quantity(value + quantity.value);
    }

    public Quantity subtract(Quantity quantity) {
        if (quantity.value < 1)
            throw new RuntimeException();

        return new Quantity(value - quantity.value);
    }

}
