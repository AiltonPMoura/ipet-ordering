package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.exception.NumberCannotBeNegativeException;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record CelPhone(Integer codArea, Integer number) {

    public CelPhone {
        FieldValidator.requiresNonNull("codArea", codArea);
        FieldValidator.requiresNonNull("number", number);
        if (codArea < 0) throw new NumberCannotBeNegativeException("celPhone codArea");
        if (number < 0) throw new NumberCannotBeNegativeException("celPhone number");
    }

}
