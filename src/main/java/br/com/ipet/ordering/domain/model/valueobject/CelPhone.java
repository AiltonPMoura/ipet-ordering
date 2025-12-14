package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.exception.NumberCannotBeNegativeException;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record CelPhone(Integer number) {

    public CelPhone {
        FieldValidator.requiresNonNull("number", number);
        if (number < 0) throw new NumberCannotBeNegativeException("celPhone number");
    }

}
