package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.exception.NumberCannotBeNegativeException;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record CelPhone(Integer value) {

    public CelPhone {
        FieldValidator.requiresNonNull("number", value);
        if (value < 0) throw new NumberCannotBeNegativeException("celPhone number");
    }

}
