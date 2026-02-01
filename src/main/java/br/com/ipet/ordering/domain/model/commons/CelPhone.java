package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.application.commons.NumberCannotBeNegativeException;

public record CelPhone(Integer value) {

    public CelPhone {
        FieldValidator.requiresNonNull("number", value);
        if (value < 0) throw new NumberCannotBeNegativeException("celPhone number");
    }

}
