package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record Name(String value) {

    public Name {
        FieldValidator.requiresNonBlank("pet name value", value);
    }

}
