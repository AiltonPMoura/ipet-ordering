package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.exception.NumberCannotBeNegativeException;
import br.com.ipet.ordering.domain.util.FieldValidator;

public record Weight(Double value) {

    public Weight {
        FieldValidator.requiresNonNull("pet weight value", value);
        if (value <= 0) throw new NumberCannotBeNegativeException("pet weight value");
    }

}
