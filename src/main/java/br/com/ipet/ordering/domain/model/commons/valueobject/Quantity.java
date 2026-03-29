package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.exception.QuantityNeedsGreaterThanZeroException;

import java.io.Serializable;

public record Quantity(Integer value) implements Serializable, Comparable<Quantity> {

    public static Quantity ZERO = new Quantity(0);

    public Quantity {
        FieldValidator.requiresNonNull("quantity value", value);

        if (value < 0)
            throw new QuantityCannotBeNegative();
    }

    public Quantity add(Quantity quantity) {
        FieldValidator.requiresNonNull("quantity", quantity);

        if (quantity.value < 1)
            throw new QuantityNeedsGreaterThanZeroException();

        return new Quantity(value + quantity.value());
    }

    @Override
    public int compareTo(Quantity other) {
        return value.compareTo(other.value());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
