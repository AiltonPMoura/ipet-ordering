package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.exception.NumberCannotBeNegativeException;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record Weight(Double value) {

    public Weight {
        FieldValidator.requiresNonNull("pet weight value", value);
        if (value <= 0) throw new NumberCannotBeNegativeException("pet weight value");
    }

}
