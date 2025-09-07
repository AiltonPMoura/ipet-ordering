package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record CelPhone(Integer codArea, Integer number) {

    public CelPhone {
        FieldValidator.notNull("codArea", codArea);
        FieldValidator.notNull("number", number);
    }

}
