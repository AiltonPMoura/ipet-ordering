package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record Phone(String value) {

    public Phone {
        FieldValidator.requiresNonNull("number", value);
    }

}
